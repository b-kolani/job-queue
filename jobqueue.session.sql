SELECT id, status, attempts, created_at, started_at, completed_at
FROM jobs
WHERE status = 'COMPLETED'
ORDER BY completed_at DESC
LIMIT 1;