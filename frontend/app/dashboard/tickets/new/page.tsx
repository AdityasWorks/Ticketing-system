'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import apiService from '../../../services/apiService';

export default function NewTicketPage() {
    const router = useRouter();
    const [subject, setSubject] = useState('');
    const [description, setDescription] = useState('');
    const [priority, setPriority] = useState('MEDIUM');
    const [file, setFile] = useState<File | null>(null);
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setFile(event.target.files[0]);
        }
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsSubmitting(true);
        setError('');

        const formData = new FormData();
        
        const ticketData = { subject, description, priority };
        formData.append('ticket', new Blob([JSON.stringify(ticketData)], { type: 'application/json' }));

        if (file) {
            formData.append('file', file);
        }

        try {
            await apiService.post('/tickets', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            router.push('/dashboard');
        } catch (err) {
            console.error('Failed to create ticket:', err);
            setError('Failed to create ticket. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <main className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">Create a New Ticket</h1>
            <div className="w-full max-w-2xl p-8 bg-white rounded-lg shadow-md">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label htmlFor="subject" className="block text-sm font-medium text-gray-700">Subject</label>
                        <input id="subject" type="text" required value={subject} onChange={(e) => setSubject(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"/>
                    </div>
                    <div>
                        <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description</label>
                        <textarea id="description" rows={6} required value={description} onChange={(e) => setDescription(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"/>
                    </div>
                    <div>
                        <label htmlFor="priority" className="block text-sm font-medium text-gray-700">Priority</label>
                        <select id="priority" value={priority} onChange={(e) => setPriority(e.target.value)}
                            className="block w-full px-3 py-2 mt-1 bg-white text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                            <option>LOW</option>
                            <option>MEDIUM</option>
                            <option>HIGH</option>
                            <option>URGENT</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="file" className="block text-sm font-medium text-gray-700">Attachment (Optional)</label>
                        <input id="file" type="file" onChange={handleFileChange}
                            className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-600 hover:file:bg-indigo-100"/>
                    </div>

                    {error && <p className="text-sm text-red-600">{error}</p>}
                    
                    <div>
                        <button type="submit" disabled={isSubmitting}
                            className="w-full px-4 py-2 font-medium text-white bg-indigo-600 border rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:bg-indigo-300">
                            {isSubmitting ? 'Submitting...' : 'Submit Ticket'}
                        </button>
                    </div>
                </form>
            </div>
        </main>
    );
}