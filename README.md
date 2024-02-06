# YOUng-chat

사용할 수록 젊어지는, 실시간 채팅 서비스

## 목차

----------------

[1. 프로젝트 소개](#1.-프로젝트-소개)

[2. 팀원 소개](#2-팀원-소개)

[3. 아키텍처](#3-아키텍처)

[4. ERD](#4-erd)

[5. 주요 기술](#5-주요-기술)

[6. 기술적 의사결정](#6-기술적-의사결정)

[7. 트러블 슈팅](#7-트러블-슈팅)

## 1. 프로젝트 소개

----

* **프로젝트 기간** : 2024.1.4 ~ 2024.2.8
* [YOUngChat 배포 사이트 바로가기](https://youngchat.store)
* [FE GitHub Repository](https://github.com/Just-Clover/YOUng-chat-frontend)

단순한 기능과 한 눈에 들어오는 UI로 남녀노소 누구나 쉽게 사용할 수 있는 **실시간 채팅 서비스** 입니다.
서비스 로직보다는 **기술적인 도전**을 더 중점적으로 진행하였습니다.

## 2. 팀원 소개

---

| ![profile](https://avatars.githubusercontent.com/u/73452223?v=4) | ![profile](https://avatars.githubusercontent.com/u/75934088?v=4) | ![profile](https://avatars.githubusercontent.com/u/109781694?v=4) |
|------------------------------------------------------------------|------------------------------------------------------------------|-------------------------------------------------------------------|
| <center>리더                                                       | <center>부리더                                                      | <center>팀원                                                        |
| <center>정해인</br> [@seaStamp](https://github.com/seaStamp)        | <center>최준영 </br> [@junxtar](https://github.com/junxtar)         | <center>용소희</br>[@yongcowhee](https://github.com/yongcowhee)      |

## 3. 아키텍처

---

![Untitled](https://github.com/Just-Clover/YOUng-chat-backend/assets/73452223/72e13a57-ece5-4838-9c73-36a6f1932f29)

## 4. ERD

---
![image](https://github.com/Just-Clover/YOUng-chat-backend/assets/73452223/27818c10-e5c2-49cf-9411-b5bc7f0632c5)

## 4. 사용 기술

---
<details>
<summary><b>BE</b></summary>

- Java 17
- Spring boot 3.1.7
- Spring Security 3.2.2
- JWT 0.11.5
- Spring Data JPA 3.1.7
- Spring Data Redis 3.1.7
- QueryDSL 5.0.0
- WebSocket 3.2.2
- RabbitMQ 3.12.12
- STOMP 3.2.2

</details>

<details>
<summary><b>FE</b></summary>

- React
- Vite
- Mui
- Zustand
- Axios
- rabbitmq-client
- stompjs

</details>

<details>
<summary><b>DB</b></summary>

- MySQL 8.0.35
- Redis 7.2
- H2 2.2.220

</details>

<details>
<summary><b>Infra</b></summary>

- EC2
- ECR
- S3
- LoadBalancer(ALB)
- CodeDeploy
- RDS(MySQL)
- Route53
- ElastiCache(Redis)
- CloudFront
- Docker

</details>

## 5. 주요 기술

---

### RabbitMQ & STOMP

> **실시간 채팅**

- 1:1 채팅 구현
- 단체 채팅 구현

> **채팅 알림**

- Message Broker인 RabbitMQ를 이용한 채팅 알림 서비스 구현

### JMeter

> **부하테스트**

- JMeter를 이용한 부하테스트 진행 → Query성능 개선 및 아키텍처 수정을 통한 성능 개선

### Spring Security & JWT & Redis

> **이메일 인증 및 회원 가입**

- 회원 가입 시 이메일 인증 진행
- Redis에 Authentication Code, User 정보 저장
- 인증 진행 후 삭제하여 재접근 제한

> **인증/인가**

- JWT를 이용한 AccessToken과 RefreshToken 발급 및 로그인 처리
- Token 방식의 회수 불가능한 단점 보완을 위한 Redis TTL 방식 로그아웃 처리

## 6. 기술적 의사결정

---

Cursor-based Pagination 도입

부하/성능 테스트 툴 - Jmeter

Polling/ WebSocket

CI / CD

로깅 전략

## 7. 트러블 슈팅

---

