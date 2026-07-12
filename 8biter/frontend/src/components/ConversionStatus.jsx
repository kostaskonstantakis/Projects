import React, { useState, useEffect } from 'react'
import axios from 'axios'

export default function ConversionStatus({ jobId, onComplete }) {
  const [status, setStatus] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const response = await axios.get(`/api/status/${jobId}`)
        setStatus(response.data)

        if (response.data.status === 'completed' || response.data.status === 'error') {
          clearInterval(interval)
          setLoading(false)
        }
      } catch (err) {
        setError('Failed to fetch status')
        clearInterval(interval)
      }
    }, 1000)

    return () => clearInterval(interval)
  }, [jobId])

  const handleDownload = async () => {
    try {
      const response = await axios.get(`/api/download/${jobId}`, {
        responseType: 'blob'
      })
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `8bit_${jobId}.wav`)
      document.body.appendChild(link)
      link.click()
      link.parentElement.removeChild(link)
    } catch (err) {
      setError('Download failed')
    }
  }

  const getStatusIcon = (status) => {
    const icons = {
      'loading': '⏳',
      'downloading': '📥',
      'separating': '🎼',
      'converting': '⚙️',
      'mixing': '🎛️',
      'saving': '💾',
      'completed': '✅',
      'error': '❌'
    }
    return icons[status] || '⏳'
  }

  return (
    <div className="text-center">
      {status && (
        <div>
          <div className="mb-8">
            <div className="text-5xl mb-4">{getStatusIcon(status.status)}</div>
            <h2 className="text-3xl font-bold text-white mb-2">{status.message}</h2>
            <p className="text-indigo-200">{status.status.toUpperCase()}</p>
          </div>

          {/* Progress Bar */}
          <div className="mb-8">
            <div className="bg-indigo-900 rounded-full h-2 overflow-hidden">
              <div
                className="bg-indigo-500 h-full transition-all duration-300"
                style={{ width: `${status.progress}%` }}
              ></div>
            </div>
            <p className="text-indigo-200 mt-2">{status.progress}%</p>
          </div>

          {error && (
            <div className="p-4 bg-red-500 bg-opacity-20 border border-red-500 rounded-lg text-red-200 mb-6">
              {error}
            </div>
          )}

          {status.status === 'completed' && (
            <div className="flex gap-4">
              <button
                onClick={handleDownload}
                className="flex-1 bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded-lg transition"
              >
                📥 Download 8-bit Audio
              </button>
              <button
                onClick={onComplete}
                className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 rounded-lg transition"
              >
                🔄 Convert Another
              </button>
            </div>
          )}

          {status.status === 'error' && (
            <button
              onClick={onComplete}
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 rounded-lg transition"
            >
              🔄 Try Again
            </button>
          )}
        </div>
      )}

      {loading && !status && (
        <div className="animate-pulse-custom text-5xl">⏳</div>
      )}
    </div>
  )
}
