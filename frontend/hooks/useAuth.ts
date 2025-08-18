import { useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  authorities: string[];
}

export function useAuth() {
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      const decodedToken: DecodedToken = jwtDecode(token);
      const userRole = decodedToken.authorities[0];
      setRole(userRole);
    }
  }, []);

  return { role };
}