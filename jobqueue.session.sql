SELECT id, status, attempts, created_at, started_at, completed_at
FROM jobs
ORDER BY created_at DESC;