delete from outgoing_detail;
delete from outgoing;
delete from inventory_detail;
delete from inventories;
delete from company;
delete from users;
INSERT INTO company (company_id) values (101);
insert into USERS(user_id,username) values(101,'아리랑'),
                                          (201,'스리랑');

insert into product(product_id,product_name,qr,product_img) values (101,'광어','',''),
                                                                   (201,'넙치','',''),
                                                                   (301,'갈치','',''),
                                                                   (401,'고등어','','');

INSERT INTO outgoing (outgoing_id, company_id, outgoing_date, outgoing_state, partial_outgoing,user_id) VALUES
                                                                                                            (101, 101, '2024-7-20', 'PENDING', true,101),
                                                                                                            (201, 101, '2024-7-28', 'READY', true,201),
                                                                                                            (301, 101, '2024-7-28', 'PENDING', true,201),
                                                                                                            (401, 101, '2024-7-28', 'PENDING', true,201),
                                                                                                            (501, 101, '2024-7-28', 'READY', true,201);
INSERT INTO outgoing_detail (detail_id, price, product_id, outgoing_id, quantity,natural_status, country, category) values (101,10000,101,101,20,'자연산','국산','활어'),
                                                                                                                           (201,10000,101,301,20,'자연산','국산','활어'),
                                                                                                                           (301,10000,201,301,20,'자연산','국산','활어');

INSERT INTO inventories(inventory_id, category, country, incoming_date, natural_status, quantity, company_id, product_id)
values (101,'활어','국산','2024-08-02','자연산',100,101,101),
       (201,'활어','국산','2024-08-02','자연산',10,101,201),
       (301,'활어','수입','2024-08-02','자연산',300,101,101),
       (401,'활어','수입','2024-08-02','자연산',400,101,201);
INSERT INTO inventory_detail(inventory_detail_id,inventory_id,company_id,warning_quantity)values (101,101,101,50)