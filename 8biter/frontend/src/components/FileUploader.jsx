import React, { useState } from 'react'
import axios from 'axios'

export default function FileUploader({ onJobStart }) {
  const [file, setFile] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0]
    if (selectedFile) {
      if (!['audio/mpeg', 'audio/wav', 'audio/flac', 'audio/ogg'].includes(selectedFile.type)) {
        setError('Please upload an audio file (MP3, WAV, FLAC, OGG)')
        return
      }
      setFile(selectedFile)
      setError(null)
    }
  }

  const handleDrop = (e) => {
    e.preventDefault()
    const droppedFile = e.dataTransfer.files[0]
    if (droppedFile) {
      setFile(droppedFile)
      setError(null)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!file) {
      setError('Please select a file')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const formData = new FormData()
      formData.append('file', file)

      const response = await axios.post('/api/convert/file', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        timeout: 10000
      })

      onJobStart(response.data.job_id)
    } catch (err) {
      setError(err.response?.data?.detail || 'Upload failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div
          onDrop={handleDrop}
          onDragOver={(e) => e.preventDefault()}
          className="border-2 border-dashed border-indigo-400 rounded-lg p-12 text-center cursor-pointer hover:border-indigo-300 transition"
        >
          {file ? (
            <div>
              <p className="text-white text-lg font-semibold mb-2">📄 {file.name}</p>
              <p className="text-indigo-200 text-sm mb-4">
                {(file.size / 1024 / 1024).toFixed(2)} MB
              </p>
            </div>
          ) : (
            <div>
              <p className="text-white text-xl font-semibold mb-2">Drag and drop your audio file</p>
              <p className="text-indigo-200 mb-4">or click to browse</p>
              <p className="text-indigo-300 text-sm">MP3, WAV, FLAC, OGG up to 500MB</p>
            </div>
          )}
          <input
            type="file"
            accept="audio/*"
            onChange={handleFileChange}
            className="hidden"
            id="file-input"
          />
          <label htmlFor="file-input" className="absolute inset-0"></label>
        </div>

        {error && (
          <div className="mt-4 p-4 bg-red-500 bg-opacity-20 border border-red-500 rounded-lg text-red-200">
            {error}
          </div>
        )}

        <button
          type="submit"
          disabled={!file || loading}
          className="mt-8 w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-600 disabled:cursor-not-allowed text-white font-bold py-3 rounded-lg transition"
        >
          {loading ? '⏳ Converting...' : '🚀 Convert to 8-bit'}
        </button>
      </form>
    </div>
  )
}
