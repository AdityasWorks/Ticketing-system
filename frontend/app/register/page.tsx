'use client';

import { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function RegisterPage() {
    const router = useRouter();
    const [name, setName] = useState('');
    const [email, setEmail] =useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        setSuccess('');

        try {
            await axios.post('http://localhost:8080/api/auth/register', {
                name,
                email,
                password,
            });

            setSuccess('Registration successful! Redirecting to login...');
            setTimeout(() => {
                router.push('/');
            }, 2000);

        } catch (err: any) {
            console.error('Registration failed:', err);
            if (err.response && err.response.data) {
                setError(err.response.data); 
            } else {
                setError('Registration failed. Please try again.');
            }
        }
    };

    return (
        <main className="flex min-h-screen flex-col items-center justify-center bg-gray-100 p-24">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
                <h1 className="text-2xl font-bold text-center text-gray-900">
                    Create an Account
                </h1>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-gray-700">Full Name</label>
                        <input id="name" type="text" required value={name} onChange={(e) => setName(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"/>
                    </div>
                     <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email Address</label>
                        <input id="email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"/>
                    </div>
                    <div>
                        <label htmlFor="password"className="block text-sm font-medium text-gray-700">Password</label>
                        <input id="password" type="password" required value={password} onChange={(e) => setPassword(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"/>
                    </div>

                    {error && <p className="text-sm text-red-600">{error}</p>}
                    {success && <p className="text-sm text-green-600">{success}</p>}

                    <div>
                        <button type="submit"
                            className="w-full px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                            Create Account
                        </button>
                    </div>
                </form>
                 <p className="mt-6 text-center text-sm text-gray-600">
                    Already have an account?{' '}
                    <Link href="/" className="font-medium text-indigo-600 hover:text-indigo-500">
                        Sign in
                    </Link>
                </p>
            </div>
        </main>
    );
}