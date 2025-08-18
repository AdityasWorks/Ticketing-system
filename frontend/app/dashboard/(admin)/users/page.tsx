'use client';

import { useEffect, useState } from 'react';
import apiService from '../../../services/apiService';

interface User {
    id: number;
    name: string;
    email: string;
    role: 'USER' | 'ADMIN' | 'SUPPORT_AGENT';
}

export default function UserManagementPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const fetchUsers = async () => {
        try {
            const response = await apiService.get('/admin/users');
            setUsers(response.data);
        } catch (err) {
            setError('Failed to fetch users.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleRoleChange = async (userId: number, newRole: string) => {
        try {
            await apiService.put(`/admin/users/${userId}/role`, { role: newRole });
            fetchUsers();
        } catch (err) {
            alert('Failed to update role.');
            console.error(err);
        }
    };
    
    const handleDeleteUser = async (userId: number) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                await apiService.delete(`/admin/users/${userId}`);
                fetchUsers();
            } catch (err) {
                alert('Failed to delete user.');
                console.error(err);
            }
        }
    };

    if (loading) return <p>Loading users...</p>;
    if (error) return <p className="text-red-600">{error}</p>;

    return (
        <main className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">User Management</h1>
            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full leading-normal">
                    <thead>
                        <tr>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Name</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Email</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Role</th>
                            <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100"></th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{user.name}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">{user.email}</td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                    <select value={user.role} onChange={(e) => handleRoleChange(user.id, e.target.value)}
                                        className="p-1 border rounded">
                                        <option>USER</option>
                                        <option>SUPPORT_AGENT</option>
                                        <option>ADMIN</option>
                                    </select>
                                </td>
                                <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm text-right">
                                    <button onClick={() => handleDeleteUser(user.id)}
                                        className="text-red-600 hover:text-red-900">
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </main>
    );
}