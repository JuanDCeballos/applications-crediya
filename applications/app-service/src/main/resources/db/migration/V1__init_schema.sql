CREATE TABLE IF NOT EXISTS states
(
    id_state bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default",
    description character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT states_pkey PRIMARY KEY (id_state)
);

CREATE TABLE IF NOT EXISTS loan_type
(
    id_loan_type bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default",
    min_amount numeric,
    max_amount numeric,
    interest_rate numeric,
    automatic_validation boolean,
    CONSTRAINT loan_type_pkey PRIMARY KEY (id_loan_type)
);

CREATE TABLE IF NOT EXISTS applications
(
    id_application bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    amount numeric,
    term integer,
    email character varying COLLATE pg_catalog."default",
    id_state bigint,
    id_loan_type bigint,
    CONSTRAINT applications_pkey PRIMARY KEY (id_application),
    CONSTRAINT id_loan_type FOREIGN KEY (id_loan_type)
        REFERENCES public.loan_type (id_loan_type) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT id_state FOREIGN KEY (id_state)
        REFERENCES public.states (id_state) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);