CREATE TABLE IF NOT EXISTS ui_error (
    id SERIAL PRIMARY KEY,
    created_at timestamp,
    status int,
    user_id UUID,
    url text,
    error_name text,
    error_message text,
    error_trace text,
    component_stack text
);
