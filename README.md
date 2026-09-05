# AgentPay ⚡ Autonomous Agentic Commerce & Contextual Dynamic Checkout Engine

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)](https://www.postgresql.org/)
[![Razorpay](https://img.shields.io/badge/Razorpay-API%20v1-0284c7?logo=razorpay)](https://razorpay.com/)
[![Tailwind CSS](https://img.shields.io/badge/TailwindCSS-v3.0-38bdf8?logo=tailwindcss)](https://tailwindcss.com/)

> **Submission for Razorpay AI Builder Internship 2026**  
> **Track:** Track 1: AI Growth & Agentic Commerce  
> **Developer:** Gonuguntla Dileep Kumar  

---

## 💡 Executive Summary & The Problem

Traditional e-commerce checkouts are friction-heavy: static brand catalogues, coupon abandonment, broken mandate flows, and manual voucher redemption. In 2026, commerce is transitioning toward **Agentic Commerce**—where autonomous AI agents negotiate, verify, and complete transactions on behalf of users.

**AgentPay** is an enterprise-grade, conversational commerce co-pilot that:
1. **Understands natural shopping intent** via an autonomous AI shopping agent.
2. **Aggregates 42+ institutional brand discounts** (BookMyShow, PVR INOX, Amazon, Zomato, Swiggy, Uber, Netflix) at pre-negotiated corporate rates (up to 15% OFF).
3. **Implements Deterministic Fintech Guardrails:** The LLM extracts intent, but calculations, orders, and cryptographic signature validations are executed strictly on the Spring Boot backend to prevent price hallucination.
4. **Provisions Real-Time B2B Digital Vouchers:** Upon successful Razorpay transaction settlement, generates 16-character alphanumeric codes and 4-digit PINs, securely persisted in PostgreSQL.

---

## 🏗️ System Architecture

```mermaid
graph TD
    A[Shopper / User Browser] -->|Natural Language Query| B[AI Shopping Assistant /api/chat]
    A -->|Selects Brand & Amount| C[Spring Boot PaymentController]
    
    subgraph Spring Boot Backend
        B -->|Intent Routing| D[AgentService]
        C -->|Deterministic Pricing Engine| E[Order Calculation in Paise]
        E -->|Create Order| F[Razorpay Java SDK v1.4.3]
        C -->|Verify Signature & Generate Voucher| G[Voucher Engine]
    end

    subgraph Razorpay Payments Infrastructure
        F -->|order_id in INR| H[Razorpay Checkout Rails]
        H -->|Payment Callback & Signature| C
    end

    subgraph Database Layer
        G -->|INSERT Voucher & Phone| I[(PostgreSQL 17 pgAdmin)]
        I -->|SELECT * FROM vouchers| C
    end