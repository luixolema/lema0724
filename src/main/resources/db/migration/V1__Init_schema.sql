CREATE TABLE rental_agreement
(
    id                  BIGINT NOT NULL,
    tool_code           VARCHAR(255),
    tool_type           VARCHAR(255),
    tool_brand          VARCHAR(255),
    rental_days         INT,
    check_out_date      date,
    due_date            date,
    daily_rental_charge DECIMAL,
    charge_days         INT,
    pre_discount_charge DECIMAL(10, 2),
    discount_percent    INT,
    discount_amount     DECIMAL(10, 2),
    final_charge        DECIMAL(10, 2),
    CONSTRAINT pk_rentalagreement PRIMARY KEY (id)
);

CREATE TABLE tool
(
    id           BIGINT       NOT NULL,
    tool_code    VARCHAR(255) NOT NULL,
    brand        VARCHAR(255),
    tool_type_id BIGINT       NOT NULL,
    CONSTRAINT pk_tool PRIMARY KEY (id)
);

CREATE TABLE tool_type
(
    id             BIGINT NOT NULL,
    name           VARCHAR(255),
    daily_charge   DECIMAL(10, 2),
    weekday_charge BOOLEAN,
    weekend_charge BOOLEAN,
    holiday_charge BOOLEAN,
    CONSTRAINT pk_tooltype PRIMARY KEY (id)
);

ALTER TABLE tool
    ADD CONSTRAINT uc_tool_tool_code UNIQUE (tool_code);

ALTER TABLE tool
    ADD CONSTRAINT FK_TOOL_ON_TOOL_TYPE FOREIGN KEY (tool_type_id) REFERENCES tool_type (id);

CREATE SEQUENCE rental_agreement_seq START WITH 100 INCREMENT BY 50;

CREATE SEQUENCE tool_seq START WITH 100 INCREMENT BY 50;

CREATE SEQUENCE tool_type_seq START WITH 100 INCREMENT BY 50;