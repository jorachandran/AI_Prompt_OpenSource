
# AI Prompt OpenSource (Llama 3)

AI Prompt Explorer is a full-stack open-source application that allows users to interact with **local Large Language Models (LLMs)** using **Ollama (Llama 3)**.  
It provides a clean React-based UI and a robust Spring Boot backend for private, secure AI prompt execution.

---

## 🚀 Tech Stack
- **Frontend:** React.js (Hooks, Context API for state).
- **Backend:** Spring Boot 3.x, Spring AI
- **LLM Engine:** Ollama (Llama 3)
- **Language:** Java 17+
- **Build Tool:** Maven

---

## 🌟 Key Features
- Local AI processing using Ollama (no cloud APIs)
- Connects to your local Ollama API (`http://localhost:11434`) for private
- Real-time prompt execution
- Clean and responsive React dashboard
- Spring AI integration for structured prompt handling
- Secure, private, and open-source architecture

---

### Usage
To use the prompt library in your own project:
1. Clone this repo.
2. Import the prompt JSON files into your application logic.

---

## 🌟 Features & Functionality
- **Ollama REST Bridge:** Wraps the local Ollama API to provide structured JSON responses to the React frontend.
- **CORS Enabled:** Fully configured to allow secure communication with the [AI_Prompt_ReactJS](https://github.com/jorachandran/AI_Prompt_ReactJS) frontend.

---

## ⚙️ Prerequisites
- Install **Ollama** → https://ollama.com  
- Pull the Llama 3 model:
  ```bash
  ollama pull llama3

---

## 💡 Why Ollama + Llama 3?
- **Privacy:** No data leaves your machine.
- **Cost:** Free to run (no API keys required).
- **Speed:** Low latency for local development and testing.


