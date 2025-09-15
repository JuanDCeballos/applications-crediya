INSERT INTO states (name, description) VALUES ('Pendiente de revisión', 'Pendiente por la revisión de un asesor');
INSERT INTO states (name, description) VALUES ('Rechazada', 'Solicitud rechazada');
INSERT INTO states (name, description) VALUES ('Revisión manual', 'Solicitud pendiente por revisión manual');
INSERT INTO states (name, description) VALUES ('Aprobado', 'Solicitud aprobada');

INSERT INTO loan_type (name, min_amount, max_amount, interest_rate, automatic_validation) VALUES ('Libre inversión', 0, 15000000, 2, true);