```
├── HomeController.java
├── ZipsaApplication.java
├── auth
│   ├── config
│   │   └── SecurityConfig.java
│   ├── filter
│   │   └── JwtAuthorizationFilter.java
│   └── utils
│       └── JwtProvider.java
├── command
│   ├── contract
│   │   ├── application
│   │   │   └── ContractService.java
│   │   ├── domain
│   │   │   ├── ContractResult.java
│   │   │   └── ContractResultRepository.java
│   │   ├── dto
│   │   └── presentation
│   │       └── ContractController.java
│   └── user
│       ├── application
│       │   └── UserService.java
│       ├── domain
│       │   ├── User.java
│       │   └── UserRepository.java
│       ├── dto
│       │   ├── LoginRequest.java
│       │   ├── LoginResponse.java
│       │   ├── UserCreateRequest.java
│       │   └── UserResponse.java
│       └── presentation
│           └── UserController.java
├── common
│   ├── config
│   │   └── WebClientConfig.java
│   ├── dto
│   │   ├── Base64Image.java
│   │   ├── ErrorResponse.java
│   │   └── SuccessResponse.java
│   ├── exception
│   │   ├── CustomException.java
│   │   ├── ExceptionMessages.java
│   │   └── GlobalExceptionHandler.java
│   └── utils
│       └── ImageExtractor.java
└── infrastructure
├── api
│   ├── clova
│   │   ├── application
│   │   │   └── ClovaOcrService.java
│   │   ├── dto
│   │   │   ├── ClovaOcrDefaultBody.java
│   │   │   ├── ClovaOcrImageBody.java
│   │   │   └── ClovaOcrMessageBody.java
│   │   └── presentation
│   │       └── ClovaController.java
│   ├── codef
│   │   ├── application
│   │   │   ├── CodefService.java
│   │   │   ├── CodefTokenPublisher.java
│   │   │   └── RSAEncryptor.java
│   │   └── presentation
│   │       └── CodefController.java
│   ├── gpt
│   │   ├── application
│   │   │   ├── ContractAnalysisTemplate.java
│   │   │   ├── GptApiService.java
│   │   │   └── GptChatRequestBuilder.java
│   │   └── presentation
│   │       └── GptApiController.java
│   └── odg
│       ├── application
│       │   └── OdgService.java
│       ├── dto
│       │   └── OdgDefaultRequest.java
│       └── presentation
│           └── OdgController.java
└── exception
└── ApiExceptionMessages.java
```