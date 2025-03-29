CREATE TABLE IF NOT EXISTS tasks_analytics (
     id serial PRIMARY KEY,
     assign_to bigint NOT NULL,
     description varchar(255),
     due_date timestamp(6),
     priority varchar(255),
     project_id bigint NOT NULL,
     status varchar(255),
     title varchar(255),
     created_at timestamp(6),
     updated_at timestamp(6),
     CONSTRAINT tasks_priority_check CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'CRITICAL')),
     CONSTRAINT tasks_status_check CHECK (status IN ('NEW', 'IN_PROGRESS', 'IN_TESTING', 'DONE'))
);
