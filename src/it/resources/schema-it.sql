CREATE TABLE "PUBLIC"."APPLICATION"(
    "APPLICATION_ID" BIGINT NOT NULL,
    "DESCRIPTION" VARCHAR(2000) NOT NULL,
    "APP_NAME" VARCHAR(255) NOT NULL,
    "OWNER" VARCHAR(255)
);

CREATE TABLE "PUBLIC"."TICKET"(
    "ID" BIGINT NOT NULL,
    "DESCRIPTION" VARCHAR(255),
    "STATUS" VARCHAR(255),
    "TITLE" VARCHAR(255),
    "APPLICATION_ID" BIGINT
);

CREATE TABLE "PUBLIC"."TICKET_RELEASE"(
    "RELEASE_FK" BIGINT,
    "TICKET_FK" BIGINT NOT NULL
);

CREATE TABLE "PUBLIC"."RELEASE"(
    "ID" BIGINT NOT NULL,
    "DESCRIPTION" VARCHAR(255),
    "RELEASE_DATE" VARCHAR(255) NOT NULL
);