alter table proposal
    drop constraint proposal_facility_id_fkey;
alter table proposal
    drop column facility_id;

create table proposal_m2m_facility
(
    proposal_id uuid not null,
    facility_id uuid not null,
    constraint proposal_fk foreign key (proposal_id) references proposal (id),
    constraint facility_fk foreign key (facility_id) references facility (id),
    PRIMARY KEY (proposal_id, facility_id)
)