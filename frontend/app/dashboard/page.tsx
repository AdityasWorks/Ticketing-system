'use client';

import { useEffect, useState } from 'react';
import apiService from '../services/apiService'; 
import Link from 'next/link'; 
import { useAuth } from '../../hooks/useAuth';

interface Ticket {
  id: number;
  subject: string;
  status: string;
  priority: string;
  createdAt: string;
}

export default function DashboardPage() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        setLoading(true);
        const response = await apiService.get('/tickets');
        setTickets(response.data);
      } catch (err) {
        setError('Failed to fetch tickets. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTickets();
  }, []);

  if (loading) {
    return <p className="text-center mt-8">Loading tickets...</p>;
  }

  if (error) {
    return <p className="text-center mt-8 text-red-600">{error}</p>;
  }

  return (
    <main className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">Your Tickets</h1>
            <Link href="/dashboard/tickets/new" className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700">
                Create New Ticket
            </Link>
      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="min-w-full leading-normal">
          <thead>
            <tr>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Subject
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Status
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Priority
              </th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Created On
              </th>
            </tr>
          </thead>
          <tbody>
            {tickets.length > 0 ? (
              tickets.map((ticket) => (
                <tr key={ticket.id}>
                  <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <Link href={`/dashboard/tickets/${ticket.id}`} className="text-indigo-600 hover:text-indigo-900 font-bold">
                        {ticket.subject}
                    </Link>
                    <p className="text-gray-900 whitespace-no-wrap">{ticket.subject}</p>
                  </td>
                  <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <span className={`relative inline-block px-3 py-1 font-semibold leading-tight ${
                        ticket.status === 'OPEN' ? 'text-green-900' : 'text-gray-900'
                      }`}>
                      <span aria-hidden className={`absolute inset-0 ${
                          ticket.status === 'OPEN' ? 'bg-green-200' : 'bg-gray-200'
                        } opacity-50 rounded-full`}></span>
                      <span className="relative">{ticket.status}</span>
                    </span>
                  </td>
                  <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <p className="text-gray-900 whitespace-no-wrap">{ticket.priority}</p>
                  </td>
                  <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <p className="text-gray-900 whitespace-no-wrap">
                      {new Date(ticket.createdAt).toLocaleDateString()}
                    </p>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={4} className="px-5 py-5 border-b border-gray-200 bg-white text-sm text-center">
                  You have not created any tickets yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </main>
  );
}