'use client';

import { useAuth } from '../../../hooks/useAuth';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function AdminLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const { role } = useAuth();
  const router = useRouter();
  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    if (role !== null) {
      if (role !== 'ADMIN') {
        router.push('/dashboard'); 
      } else {
        setIsAuthorized(true); 
      }
    }
  }, [role, router]);

  if (!isAuthorized) {
    return <p className="text-center mt-8">Checking permissions...</p>;
  }

  return <>{children}</>;
}