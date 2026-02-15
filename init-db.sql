-- Initialize agricultural database with sample crops and regions

CREATE EXTENSION IF NOT EXISTS uuid-ossp;

-- Sample data for crops and regions
CREATE TABLE IF NOT EXISTS crop_master (
    id SERIAL PRIMARY KEY,
    crop_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS region_master (
    id SERIAL PRIMARY KEY,
    region_name VARCHAR(100) NOT NULL UNIQUE,
    state VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample crops
INSERT INTO crop_master (crop_name) VALUES
('wheat'),
('rice'),
('corn'),
('cotton'),
('sugarcane'),
('soybean'),
('rapeseed')
ON CONFLICT DO NOTHING;

-- Insert sample Indian regions
INSERT INTO region_master (region_name, state) VALUES
('punjab', 'punjab'),
('haryana', 'haryana'),
('maharashtra', 'maharashtra'),
('karnataka', 'karnataka'),
('madhya-pradesh', 'madhya pradesh'),
('uttar-pradesh', 'uttar pradesh'),
('gujarati', 'gujarati'),
('rajasthan', 'rajasthan'),
('west-bengal', 'west bengal'),
('bihar', 'bihar')
ON CONFLICT DO NOTHING;

-- Create indexes for frequently queried columns
CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_price_crop_region ON prices(crop, region);
CREATE INDEX IF NOT EXISTS idx_price_date ON prices(price_date);
CREATE INDEX IF NOT EXISTS idx_alert_user ON price_alerts(user_id);
CREATE INDEX IF NOT EXISTS idx_prediction_date ON price_predictions(prediction_date);

