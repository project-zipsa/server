```
└── navi4
    └── zipsa
        ├── ZipsaApplication.java
        ├── auth
        │   ├── config
        │   │   └── SecurityConfig.java
        │   ├── filter
        │   │   └── JwtAuthorizationFilter.java
        │   └── utils
        │       └── JwtProvider.java
        ├── command
        │   ├── JeonseContract
        │   │   ├── application
        │   │   │   └── ContractService.java
        │   │   ├── domain
        │   │   │   ├── ContractResult.java
        │   │   │   ├── ContractResultRepository.java
        │   │   │   ├── LeaseContractAnalysisTemplate.java
        │   │   │   ├── PropertyTitleExtractionTemplate.java
        │   │   │   └── TotalContractAnalysisTemplate.java
        │   │   └── presentation
        │   │       └── ContractController.java
        │   ├── JeonseMarketPrice
        │   │   ├── application
        │   │   │   └── MarketPriceService.java
        │   │   ├── dto
        │   │   │   ├── AreaRangeType.java
        │   │   │   ├── BuiltYearRangeType.java
        │   │   │   ├── ContractType.java
        │   │   │   ├── FloorRangeType.java
        │   │   │   ├── HousingType.java
        │   │   │   ├── MarketPriceRequest.java
        │   │   │   └── MarketPriceResponse.java
        │   │   └── presentation
        │   │       └── MarketPriceController.java
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
        │       ├── CsvReader.java
        │       └── ImageExtractor.java
        └── infrastructure
            ├── api
            │   ├── clova
            │   │   ├── application
            │   │   │   └── ClovaOCRService.java
            │   │   ├── dto
            │   │   │   ├── ClovaOCRImageBody.java
            │   │   │   └── ClovaOCRMessageBody.java
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
            │   │   └── application
            │   │       ├── GptApiService.java
            │   │       └── GptChatRequestBuilder.java
            │   └── odg
            │       ├── application
            │       │   └── OdgService.java
            │       └── dto
            │           ├── BrExposInfoRequest.java
            │           ├── BrJijiguInfoRequest.java
            │           ├── BrTitleInfoRequest.java
            │           ├── OdgDefaultRequest.java
            │           └── TotalBrInfoRequest.java
            └── exception
                └── ErrorMessage.java
```