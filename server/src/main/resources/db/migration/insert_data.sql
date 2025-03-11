-- Tabla Financing_Profile
insert into financing_profile (knowledge_level, risk_profile, expenses_monthly, income_monthly,
                                             patrimony_total, percentage_save, saving_total, total_debt)
values (1, 'INTERMEDIO', 12.5, 200, 202.73, 100.88, 2.3, 200036),
       (2, 'NOVATO', 458.6, 145, 1896.12, 1239.698, 2.9, 124789),
       (3, 'INTERMEDIO', 78.96, 4789, 1458.9, 1258.97, 12.89, 1258.96);

-- Actualizar financing_profile id a Usuario
UPDATE users
SET financing_profile_id =1
where users.id = 1;

UPDATE users
SET financing_profile_id = 2
where users.id = 2;

UPDATE users
SET financing_profile_id =3
where users.id = 3;


-- Tabla Objectives
insert into objectives (id, amount_objective, annual_progress, description, frequency, objectuve_type, priority,
                                start_date, target_date)
values (1, 200.5, 20.8, 'Descripcion 1', 'MENSUAL', 'INVERSIONES', 'BAJA', '2005-01-14 13:14:01.000000',
        '2006-01-14 13:14:22.000000'),
       (2, 188.96, 12.9, 'Descripcion 2', 'UNICO', 'RETIRO', 'MEDIA', '2023-05-14 13:15:53.000000',
        '2024-06-19 13:15:59.000000'),
       (3, 199.9, 12.5, 'Descripcion 3', 'MENSUAL', 'INVERSIONES', 'ALTA', '2024-10-14 13:17:10.000000',
        '2024-11-14 13:17:16.000000');


-- Tabla Instrumento
INSERT INTO instruments (ticker, name, investment_type, sector, quotes, description, divisa, state)
VALUES
-- Instrumentos de Renta Fija
('BND123', 'Bonos Nacionales 2025', 'RENTA_FIJA', 'Gobierno', 102.5, 'Bono del gobierno con vencimiento en 2025', 'USD',
 TRUE),
('BND456', 'Bonos Corporativos AAA', 'RENTA_FIJA', 'Corporativo', 99.8,
 'Bono de alta calidad emitido por empresa multinacional', 'EUR', TRUE),

-- Instrumentos de Renta Variable
('ACC789', 'Acciones TechCorp', 'RENTA_VARIABLE', 'Tecnología', 150.75, 'Acciones de una empresa líder en tecnología',
 'USD', TRUE),
('ACC012', 'Acciones EcoEnergy', 'RENTA_VARIABLE', 'Energía', 85.3, 'Acciones de una compañía de energía renovable',
 'USD', TRUE),
('ACC345', 'Acciones PharmaLife', 'RENTA_VARIABLE', 'Salud', 120.4, 'Acciones de una farmacéutica innovadora', 'USD',
 FALSE),

-- Instrumentos Inmobiliarios
('FND567', 'Fondo Inmobiliario Urbano', 'INMUEBLES', 'Bienes Raíces', 250.0,
 'Fondo de inversión en propiedades urbanas', 'USD', TRUE),
('FND890', 'Fondo Comercial Regional', 'INMUEBLES', 'Bienes Raíces', 300.5,
 'Fondo enfocado en centros comerciales regionales', 'EUR', TRUE),
('FND234', 'Fondo Residencial Premium', 'INMUEBLES', 'Bienes Raíces', 400.0,
 'Fondo especializado en propiedades residenciales de lujo', 'USD', TRUE);


INSERT INTO recommendations (user_id, instrument_id, recommendation_type, motion, create_date, state)
VALUES (1, 1, 'COMPRA', 'Alta proyección de crecimiento en el sector gubernamental.', '2025-01-01', TRUE),
       (1, 3, 'MANTENER', 'Empresa tecnológica con buen desempeño reciente.', '2025-01-02', TRUE),
       (2, 5, 'VENTA', 'Pérdida de participación de mercado en la industria farmacéutica.', '2025-01-03', FALSE),
       (2, 7, 'COMPRA', 'Alta demanda de bienes raíces urbanos.', '2025-01-04', TRUE),
       (3, 2, 'VENTA', 'Desempeño inestable del emisor corporativo.', '2025-01-05', TRUE),
       (3, 8, 'MANTENER', 'Estabilidad en centros comerciales regionales.', '2025-01-06', TRUE);


--  Tabla Portafolio
INSERT INTO portfolio (user_id, instrument_id, quantity, purchase_price, purchase_date)
VALUES (1, 1, 100, 102.5, '2024-12-31'),
       (1, 3, 50, 150.75, '2025-01-02'),
       (2, 5, 200, 120.4, '2023-01-03'),
       (2, 7, 150, 250.0, '2025-01-04'),
       (3, 8, 75, 300.5, '2025-01-05');


-- Tabla Movimiento
INSERT INTO transactions (portfolio_id, transaction_type, quantity, unit_price, commission)
VALUES (1, 'COMPRA', 100, 150.75, 15.),
       (2, 'VENTA', 50, 120.4, 12.0),
       (3, 'COMPRA', 200, 99.8, 20.0),
       (4, 'VENTA', 150, 250.0, 25.0),
       (1, 'COMPRA', 300, 85.3, 8.5),
       (2, 'VENTA', 75, 300.5, 30.0);


-- Tabla Gastos
INSERT INTO costs (user_id, type, description, category, amount, date, update_date)
VALUES (1, 'Fijo', 'Suscripción mensual a plataforma de análisis financiero.', 'Servicios', 20.0, '2025-01-01',
        '2025-01-01'),
       (1, 'Variable', 'Compra de literatura financiera.', 'Educación', 35.5, '2025-01-02', '2025-01-02'),
       (2, 'Fijo', 'Tarifa de mantenimiento de cuenta.', 'Bancario', 15.0, '2025-01-03', '2025-01-03'),
       (2, 'Variable', 'Comisión por operación bursátil.', 'Inversiones', 50.0, '2025-01-04', '2025-01-04'),
       (3, 'Fijo', 'Servicio de almacenamiento en la nube.', 'Tecnología', 10.0, '2025-01-05', '2025-01-05');

-- Tabla Notificacion
INSERT INTO notifications (user_id, type_notification, message, date_create, is_read)
VALUES (1, 'RECOMENDACION', 'Revisar nueva recomendación para Bonos Nacionales.', '2025-01-01', FALSE),
       (1, 'ALERTA_MERCADO', 'El mercado tecnológico muestra alta volatilidad.', '2025-01-02', TRUE),
       (2, 'OBJETIVO', 'Has alcanzado tu objetivo de ahorro mensual.', '2025-01-03', TRUE),
       (2, 'SISTEMA', 'Actualización completada en la base de datos.', '2025-01-04', FALSE),
       (3, 'RECOMENDACION', 'Considera vender Acciones PharmaLife.', '2025-01-05', TRUE);


-- Tabla Wallet
INSERT INTO wallet (user_id, current_balance, date_last_update)
VALUES (1, 1500.75, '2025-01-01'),
       (1, 2500.50, '2025-01-02'),
       (2, 1000.00, '2025-01-03'),
       (2, 3000.00, '2025-01-04'),
       (3, 500.25, '2025-01-05');

INSERT INTO notice (id, author, comments, description, image_url, published_at, title)
VALUES
      (1, 'Ana Beltrán', 74, 'Un análisis detallado de cómo las economías más grandes del mundo están enfrentando el año 2025.', 'https://example.com/economia-2025.jpg', '2025-02-03T12:00:00Z', 'La economía global en 2025'),
      (2, 'Miguel Torres', 44, 'Explorando las tendencias y predicciones para el mercado de criptomonedas en el próximo año.', 'https://example.com/criptomonedas.jpg', '2025-02-01T10:30:00Z', 'El futuro de las criptomonedas'),
      (3, 'Luisa Martínez', 59, 'Un análisis sobre cómo DeepSeek está transformando la informática en el ámbito de la programación y las finanzas.', 'https://example.com/ia-medicina.jpg', '2025-01-30T08:15:00Z', 'Impacto de la inteligencia artificial en la medicina'),
      (4, 'Elena Vargas', 39, 'Una exploración de las nuevas tecnologías que están revolucionando el sector automotriz.', 'https://example.com/auto-tech.jpg', '2025-01-20T14:45:00Z', 'Tecnologías emergentes en la industria automotriz');