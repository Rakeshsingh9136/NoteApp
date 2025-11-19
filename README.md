# NoteApp
Note app
🚀 What the App Does
📝 1. Smart Notes

Create notes with text, voice, or images

Automatic text extraction from images (OCR via ML Kit)

📌 2. Intelligent Task Manager

Natural-language task creation

User types: “remind me to call mom tomorrow at 6 PM”

App auto-detects date → time → category

⏰ 3. Context-Aware Reminders

Location-based reminders

“Remind me to buy milk when I reach the store”

Time-based reminders

Smart snooze suggestions (machine-learned patterns)

🧠 4. AI Insights Engine

Identifies patterns such as:

Tasks you postpone frequently

Notes you revisit often

Suggests:

Optimal times for tasks

Automatic tagging of notes

Weekly priority summary

☁️ 5. Offline-First Sync

Local Room database

Cloud sync with Firebase (or Supabase/Appwrite) when online

🏗️ Tech Stack (Modern, Resume-Ready)
Frontend

Jetpack Compose (100% UI)

Material 3 + Animations

Navigation Compose

Architecture

Clean Architecture

MVVM

Modularization (feature modules)

Coroutines + Flow

Data & APIs

Room (offline database)

Retrofit (if you add external APIs)

Firebase Firestore (cloud sync)

AI/ML

ML Kit:

Text recognition (OCR)

Entity extraction (dates, times, locations)

On-device ML model for:

Smart suggestions

Habit patterns

Other Important Tools

Hilt (DI)

WorkManager (background sync & reminders)

Maps SDK (location reminders)
