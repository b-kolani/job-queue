SELECT id, status, started_at
FROM jobs
WHERE status = 'PROCESSING'
AND started_at < NOW() - INTERVAL '15 seconds';