CREATE TABLE jobs (
    id UUID PRIMARY KEY,

    type VARCHAR NOT NULL,

    payload JSONB NOT NULL,

    status VARCHAR NOT NULL DEFAULT 'PENDING' CHECK(status IN (
        'PENDING',
        'PROCESSING',
        'COMPLETED',
        'FAILED'
    )),

    attempts INTEGER NOT NULL DEFAULT 0 CHECK (attempts >= 0),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_jobs_status_created_at
ON jobs (status, created_at);