import React, { useState } from 'react'
import FileUploader from './components/FileUploader'
import URLConverter from './components/URLConverter'
import ConversionStatus from './components/ConversionStatus'
import './App.css'

export default function App() {
  const [activeTab, setActiveTab] = useState('upload')
  const [jobId, setJobId] = useState(null)
  const [conversionStarted, setConversionStarted] = useState(false)

  const handleJobStart = (id) => {
    setJobId(id)
    setConversionStarted(true)
  }

  const handleConversionComplete = () => {
    setConversionStarted(false)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-900 to-purple-900">
      <div className="container mx-auto px-4 py-12">
        {/* Header */}
        <header className="text-center mb-12">
          <h1 className="text-5xl font-bold text-white mb-4">🎵 8-bit Audio Converter</h1>
          <p className="text-xl text-indigo-200">
            Transform modern music into authentic 8-bit chiptune while preserving all instruments
          </p>
        </header>

        {/* Main Content */}
        <div className="max-w-4xl mx-auto">
          {!conversionStarted ? (
            <>
              {/* Tabs */}
              <div className="flex gap-4 mb-8">
                <button
                  onClick={() => setActiveTab('upload')}
                  className={`px-6 py-3 rounded-lg font-semibold transition ${
                    activeTab === 'upload'
                      ? 'bg-indigo-600 text-white'
                      : 'bg-indigo-800 text-indigo-200 hover:bg-indigo-700'
                  }`}
                >
                  📁 Upload File
                </button>
                <button
                  onClick={() => setActiveTab('url')}
                  className={`px-6 py-3 rounded-lg font-semibold transition ${
                    activeTab === 'url'
                      ? 'bg-indigo-600 text-white'
                      : 'bg-indigo-800 text-indigo-200 hover:bg-indigo-700'
                  }`}
                >
                  🔗 From URL
                </button>
              </div>

              {/* Content */}
              <div className="bg-white bg-opacity-10 backdrop-blur-md rounded-xl p-8 border border-white border-opacity-20">
                {activeTab === 'upload' && (
                  <FileUploader onJobStart={handleJobStart} />
                )}
                {activeTab === 'url' && (
                  <URLConverter onJobStart={handleJobStart} />
                )}
              </div>
            </>
          ) : (
            <div className="bg-white bg-opacity-10 backdrop-blur-md rounded-xl p-8 border border-white border-opacity-20">
              <ConversionStatus 
                jobId={jobId} 
                onComplete={handleConversionComplete}
              />
            </div>
          )}
        </div>

        {/* Features */}
        <div className="mt-16 grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="bg-white bg-opacity-5 rounded-lg p-6 border border-white border-opacity-10">
            <h3 className="text-xl font-bold text-white mb-3">🎸 Source Separation</h3>
            <p className="text-indigo-200">AI-powered separation preserves vocals, drums, bass, and other instruments</p>
          </div>
          <div className="bg-white bg-opacity-5 rounded-lg p-6 border border-white border-opacity-10">
            <h3 className="text-xl font-bold text-white mb-3">📊 Smart Quantization</h3>
            <p className="text-indigo-200">Advanced noise shaping maintains audio quality during bit-depth reduction</p>
          </div>
          <div className="bg-white bg-opacity-5 rounded-lg p-6 border border-white border-opacity-10">
            <h3 className="text-xl font-bold text-white mb-3">🌐 Multi-Source</h3>
            <p className="text-indigo-200">YouTube, Spotify, SoundCloud, or upload your own files</p>
          </div>
        </div>
      </div>
    </div>
  )
}
