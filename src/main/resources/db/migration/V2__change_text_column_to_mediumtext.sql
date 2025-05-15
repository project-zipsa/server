-- 1. jeonse_contract_text 컬럼을 MEDIUMTEXT로 변경
ALTER TABLE contract_results
    MODIFY COLUMN jeonse_contract_text MEDIUMTEXT;

-- 2. property_title_text 컬럼을 MEDIUMTEXT로 변경
ALTER TABLE contract_results
    MODIFY COLUMN property_title_text MEDIUMTEXT;
