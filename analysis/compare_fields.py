import json
import re
import os

ROOT = r"E:\learn\car-listing"
JSON_PATH = os.path.join(ROOT, 'analysis', 'sample_listings.json')
DTO_DIR = os.path.join(ROOT, 'backend', 'src', 'main', 'java', 'lk', 'amila', 'cars', 'controller', 'dto')

with open(JSON_PATH, 'r', encoding='utf-8') as f:
    data = json.load(f)

listings = data.get('pageProps', {}).get('listings', [])
if not listings:
    print('No listings found in sample JSON')
    raise SystemExit(1)

# collect union of top-level keys
json_top_keys = set()
for l in listings:
    json_top_keys.update(l.keys())

print('Top-level keys in JSON (union):')
print(sorted(json_top_keys))

# parse DTO files to collect classes and public fields
classes = {}
for fn in os.listdir(DTO_DIR):
    if not fn.endswith('.java'):
        continue
    path = os.path.join(DTO_DIR, fn)
    with open(path, 'r', encoding='utf-8') as f:
        txt = f.read()
    # try to find public class Name
    m = re.search(r'public\s+class\s+(\w+)', txt)
    if not m:
        continue
    cls = m.group(1)
    fields = re.findall(r'public\s+[\w<>\.\[\]]+\s+(\w+)\s*;', txt)
    classes[cls] = set(fields)

print('\nFound DTO classes and fields:')
for cls, fields in classes.items():
    print(f' - {cls}: {sorted(fields)}')

# mapping from JSON key to DTO class name heuristically
def to_class_name(key):
    # camelCase to PascalCase
    return key[0].upper() + key[1:]

# Check top-level keys against Listing DTO fields
listing_fields = classes.get('Listing', set())
missing_top = []
for k in sorted(json_top_keys):
    if k not in listing_fields:
        missing_top.append(k)

print('\nTop-level JSON keys missing in Listing DTO:')
print(sorted(missing_top))

# For nested objects, check nested keys vs corresponding DTO
nested_issues = {}
for k in sorted(list(json_top_keys)):
    val = None
    # find first listing that has this key
    for l in listings:
        if k in l:
            val = l[k]
            break
    if val is None:
        continue
    # determine dto class
    possible = to_class_name(k)
    # handle plurals: vehicleDetails -> VehicleDetail
    if possible.endswith('s') and possible[:-1] in classes:
        possible = possible[:-1]
    # special mapping for trackingParameters -> TrackingParameter
    if possible == 'TrackingParameters':
        possible = 'TrackingParameter'
    # seals -> Seal
    if possible == 'Seals':
        possible = 'Seal'
    if possible not in classes:
        continue
    # get json nested keys
    nested_keys = set()
    if isinstance(val, list) and val:
        if isinstance(val[0], dict):
            for item in val:
                nested_keys.update(item.keys())
    elif isinstance(val, dict):
        nested_keys.update(val.keys())
    else:
        continue
    dto_fields = classes.get(possible, set())
    missing = sorted([nk for nk in nested_keys if nk not in dto_fields])
    if missing:
        nested_issues[k] = {'dto': possible, 'missing_fields': missing, 'json_sample_keys': sorted(nested_keys)}

print('\nNested JSON fields missing in DTOs:')
for k, info in nested_issues.items():
    print(f"- top key '{k}' -> DTO {info['dto']}: missing fields: {info['missing_fields']}, sample keys: {info['json_sample_keys']}")

# Also report any DTO fields not present in JSON (optional)

extra_dto_fields = {}
for cls, fields in classes.items():
    if cls == 'Listing':
        continue
    # skip small DTOs
    extra = [f for f in fields if f not in ['id']]
    extra_dto_fields[cls] = extra

print('\nAnalysis complete.')

