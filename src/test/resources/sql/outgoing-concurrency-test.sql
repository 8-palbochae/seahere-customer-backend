delete from product;
delete from outgoing_detail;
delete from outgoing;
delete from inventory_detail;
delete from inventories;
delete from company;
delete from users;

insert into product(product_id, product_name) values (1,'광어');
insert into company(company_id, company_name) values (1, '동시성수산');
insert into users(user_id,email,company_id) values (1, 'test@naver.com',1);
insert into users(user_id,username) values (2,'도라에몽');
insert into inventories(inventory_id, category, country, natural_status, quantity, company_id, product_id)
values (1,'활어','국산','양식',30,1,1);
insert into inventory_detail(inventory_detail_id, outgoing_price, warning_quantity, company_id, inventory_id)
values (1,1000,100,1,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (0, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (0,'활어','국산','양식',1,'ACTIVE',0,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (1, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (1,'활어','국산','양식',1,'ACTIVE',1,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (2, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (2,'활어','국산','양식',1,'ACTIVE',2,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (3, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (3,'활어','국산','양식',1,'ACTIVE',3,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (4, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (4,'활어','국산','양식',1,'ACTIVE',4,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (5, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (5,'활어','국산','양식',1,'ACTIVE',5,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (6, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (6,'활어','국산','양식',1,'ACTIVE',6,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (7, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (7,'활어','국산','양식',1,'ACTIVE',7,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (8, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (8,'활어','국산','양식',1,'ACTIVE',8,1);

insert into outgoing(outgoing_id,outgoing_state,partial_outgoing,trade_type,company_id,user_id)
values (9, 'PENDING',true, 'b2c',1,2);
insert into outgoing_detail(detail_id, category,country, natural_status, quantity,state, outgoing_id,product_id)
values (9,'활어','국산','양식',1,'ACTIVE',9,1);