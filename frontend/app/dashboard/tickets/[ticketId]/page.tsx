'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import apiService from '../../..//services/apiService';

interface Attachment {
  id: number;
  fileName: string;
  fileUrl: string;
}


interface Comment {
    id: number;
    text: string;
    authorName: string;
    createdAt: string;
}


interface Ticket {
    id: number;
    subject: string;
    description: string;
    status: string;
    priority: string;
    requesterName: string;
    assigneeName: string | null;
    createdAt: string;
    updatedAt: string;
    comments: Comment[];
    attachments: Attachment[];
}

export default function TicketDetailPage() {
    const params = useParams();
    const ticketId = params.ticketId;

    const [ticket, setTicket] = useState<Ticket | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [newComment, setNewComment] = useState('');

    const fetchTicket = async () => {
        try {
            const response = await apiService.get(`/tickets/${ticketId}`);
            setTicket(response.data);
        } catch (err) {
            setError('Failed to fetch ticket details.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (ticketId) {
            fetchTicket();
        }
    }, [ticketId]); 

    const handleCommentSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!newComment.trim()) return;

        try {
            await apiService.post(`/tickets/${ticketId}/comments`, { text: newComment });
            setNewComment('');
            fetchTicket();
        } catch (err) {
            setError('Failed to post comment. Please try again.');
            console.error(err);
        }
    };

    if (loading) return <p className="text-center mt-8">Loading ticket...</p>;
    if (error) return <p className="text-center mt-8 text-red-600">{error}</p>;
    if (!ticket) return <p className="text-center mt-8">Ticket not found.</p>;

    return (
        <main className="container mx-auto p-8">
            <div className="bg-white shadow-md rounded-lg p-6">
                <h1 className="text-3xl font-bold mb-2">{ticket.subject}</h1>
                <div className="flex space-x-4 text-sm text-gray-500 mb-4">
                    <span>Status: <span className="font-semibold">{ticket.status}</span></span>
                    <span>Priority: <span className="font-semibold">{ticket.priority}</span></span>
                    <span>Requester: <span className="font-semibold">{ticket.requesterName}</span></span>
                    <span>Assignee: <span className="font-semibold">{ticket.assigneeName || 'Unassigned'}</span></span>
                </div>
                <div className="prose max-w-none">
                    <p>{ticket.description}</p>
                </div>

                {ticket.attachments && ticket.attachments.length > 0 && (
                    <div className="mt-6">
                        <h3 className="text-lg font-semibold border-t pt-4">Attachments</h3>
                        <ul className="list-disc list-inside mt-2 space-y-1">
                            {ticket.attachments.map(file => (
                                <li key={file.id}>
                                    <a href={file.fileUrl} target="_blank" rel="noopener noreferrer"
                                       className="text-indigo-600 hover:text-indigo-900 hover:underline">
                                        {file.fileName}
                                    </a>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            <div className="mt-8">
                <h2 className="text-2xl font-bold mb-4">Comment History</h2>
                <div className="space-y-4">
                    {ticket.comments.map(comment => (
                        <div key={comment.id} className="bg-white shadow-md rounded-lg p-4">
                            <p className="text-gray-800">{comment.text}</p>
                            <div className="text-right text-xs text-gray-500 mt-2">
                                - {comment.authorName} on {new Date(comment.createdAt).toLocaleString()}
                            </div>
                        </div>
                    ))}
                    {ticket.comments.length === 0 && <p className="text-gray-500">No comments yet.</p>}
                </div>
            </div>

            <div className="mt-8">
                <h2 className="text-2xl font-bold mb-4">Add a Comment</h2>
                <form onSubmit={handleCommentSubmit} className="bg-white shadow-md rounded-lg p-4">
                    <textarea
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        rows={4}
                        className="block w-full px-3 py-2 text-gray-900 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                        placeholder="Type your comment here..."
                    />
                    <button type="submit" className="mt-4 px-4 py-2 font-medium text-white bg-indigo-600 border rounded-md shadow-sm hover:bg-indigo-700">
                        Add Comment
                    </button>
                </form>
            </div>
        </main>
    );
}