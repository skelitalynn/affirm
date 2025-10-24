CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS affirm_templates (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  content VARCHAR(300) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_template_user_content (user_id, content),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS affirm_counters (
  user_id BIGINT PRIMARY KEY,
  total_count BIGINT DEFAULT 0,
  last_affirm_at TIMESTAMP NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS affirm_events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  template_id BIGINT NULL,
  source VARCHAR(20),
  note VARCHAR(300),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  KEY idx_events_user_time (user_id, created_at),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS milestones (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  threshold BIGINT NOT NULL,
  label VARCHAR(50) NOT NULL,
  UNIQUE KEY uq_milestone_threshold (threshold)
);

CREATE TABLE IF NOT EXISTS user_milestones (
  user_id BIGINT NOT NULL,
  milestone_id BIGINT NOT NULL,
  achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, milestone_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (milestone_id) REFERENCES milestones(id) ON DELETE CASCADE
);

INSERT INTO milestones (threshold, label)
SELECT * FROM (SELECT 5000 AS threshold, 'Bronze Believer' AS label) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM milestones WHERE threshold = 5000);

INSERT INTO milestones (threshold, label)
SELECT * FROM (SELECT 7500 AS threshold, 'Silver Seeker' AS label) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM milestones WHERE threshold = 7500);

INSERT INTO milestones (threshold, label)
SELECT * FROM (SELECT 10000 AS threshold, 'Golden Grit' AS label) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM milestones WHERE threshold = 10000);


