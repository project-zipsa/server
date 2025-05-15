-- 1. 기존 외래키 삭제
ALTER TABLE contract_results
    DROP FOREIGN KEY user_id;

-- 2. 새로운 외래키 제약 조건 생성 (ON DELETE CASCADE 포함)
ALTER TABLE contract_results
    ADD CONSTRAINT user_id
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE;

-- 3. users_id에 UNIQUE 제약 추가 → 1:1 관계 보장
ALTER TABLE contract_results
    ADD CONSTRAINT uq_contract_results_user UNIQUE (user_id);