import Link from 'next/link';
import React from 'react';
import { useRouter } from 'next/router';
import navItems from '../config/nav';

export default function Layout({ children, title = 'Car Listing' }) {
  const router = useRouter();

  return (
    <div style={{ display: 'flex', minHeight: '100vh', fontFamily: 'Arial, sans-serif' }}>
      <aside style={{ width: 220, borderRight: '1px solid #eee', padding: 20, boxSizing: 'border-box' }}>
        <div style={{ fontSize: 14, fontWeight: 700, marginBottom: 12 }}>Sections</div>
        <nav style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {navItems.filter(n => n.showInNav !== false).map((item) => {
            const isActive = router.pathname === item.href;
            return (
              <Link key={item.href} href={item.href} style={{ textDecoration: 'none' }}>
                <span
                  style={{
                    display: 'block',
                    padding: '6px 8px',
                    borderRadius: 6,
                    background: isActive ? '#e9ecef' : 'transparent',
                    color: isActive ? '#111' : '#111',
                    fontWeight: isActive ? 700 : 500,
                  }}
                >
                  {item.label}
                </span>
              </Link>
            );
          })}
        </nav>
      </aside>

      <main style={{ flex: 1, padding: 20 }}>{children}</main>
    </div>
  );
}
