import React, { useState } from 'react'
import axios from 'axios'

export default function URLConverter({ onJobStart }) {
  const [url, setUrl] = useState('')
  const [source, setSource] = useState('youtube')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!url.trim()) {
      setError('Please enter a URL')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await axios.post('/api/convert/url', {
        url: url,
        source: source
      })

      onJobStart(response.data.job_id)
    } catch (err) {
      setError(err.response?.data?.detail || 'Conversion failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div className="mb-6">
          <label className="block text-white font-semibold mb-2">Source</label>
          <select
            value={source}
            onChange={(e) => setSource(e.target.value)}
            className="w-full px-4 py-2 rounded-lg bg-indigo-900 text-white border border-indigo-700 focus:outline-none focus:border-indigo-500"
          >
            <option value="youtube">YouTube</option>
            <option value="spotify">Spotify</option>
            <option value="soundcloud">SoundCloud</option>
          </select>
        </div>

        <div className="mb-6">
          <label className="block text-white font-semibold mb-2">URL</label>
          <input
            type="url"
            placeholder={
              source === 'youtube' ? 'https://youtube.com/watch?v=...' :
              source === 'spotify' ? 'https://open.spotify.com/track/...' :
              'https://soundcloud.com/...'
            }
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            className="w-full px-4 py-2 rounded-lg bg-indigo-900 text-white border border-indigo-700 placeholder-indigo-400 focus:outline-none focus:border-indigo-500"
          />
        </div>

        {error && (
          <div className="mb-6 p-4 bg-red-500 bg-opacity-20 border border-red-500 rounded-lg text-red-200">
            {error}
          </div>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-600 disabled:cursor-not-allowed text-white font-bold py-3 rounded-lg transition"
        >
          {loading ? '⏳ Processing...' : '🚀 Convert to 8-bit'}
        </button>
      </form>
    </div>
  )
}
