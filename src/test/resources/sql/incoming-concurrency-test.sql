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