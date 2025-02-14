CREATE TABLE demo.students (
	id int4 NOT NULL,
	"name" varchar(255) NULL,
	address_id int4 NULL,
	department_id int4 NULL,
	department varchar(255) NULL,
	is_active BOOLEAN DEFAULT true,         -- Whether the student is currently active
    created_date timestamp(6),
    modified_date timestamp(6),
	CONSTRAINT student_pkey PRIMARY KEY (id)
);

CREATE TABLE demo.department (
	departmanet_id int4 NOT NULL,
	departmanet varchar(255) NULL,
    is_active BOOLEAN DEFAULT true,         -- Whether the student is currently active
    created_date timestamp(6),
    modified_date timestamp(6),
	CONSTRAINT department_pkey PRIMARY KEY (departmanet_id)
);

CREATE TABLE demo.address (
	address_id int4 NOT NULL,
	address varchar(255) NULL,
    is_active BOOLEAN DEFAULT true,         -- Whether the student is currently active
    created_date timestamp(6),
    modified_date timestamp(6),
	CONSTRAINT address_pkey PRIMARY KEY (address_id)
);