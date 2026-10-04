SELECT * 
FROM jobs 
WHERE status = 'PENDING'
ORDER BY created_at ASC
LIMIT 1;
