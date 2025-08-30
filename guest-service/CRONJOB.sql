#Active Regular test case check
SELECT id,amount_paid,guest_status,check_in_date,default_rent,due_amount,last_bill_generation_date,occupancy_type,planned_check_out_date,
next_dues_generation,paidtill,bill_generated_till,over_all_due,building_id,bed_id
 FROM buildingdb.guest  where guest_status="ACTIVE"
 and building_id=1;
 select * from payments where guest_id="SLH000745";
call guest_due("SLH000792");

 #get Innotice and regular guest test case
 SELECT id,amount_paid,guest_status,check_in_date,default_rent,due_amount,last_bill_generation_date,
next_dues_generation,paidtill,bill_generated_till,over_all_due,building_id
 FROM buildingdb.guest where occupancy_type="Regular" AND guest_status="InNotice"
 and  building_id=1;
 
 select id,amount_paid,guest_status,check_in_date,default_rent,due_amount,last_bill_generation_date,bed_id,
	next_dues_generation,paidtill,bill_generated_till,over_all_due ,security_deposit, building_id,planned_check_out_date 
    from guest where
    id="SLH000940" ;
 
use buildingdb;

# to get active regular due count by building ID
SELECT count(*) FROM buildingdb.guest where due_amount>0 and guest_status="active" 
and occupancy_type="Regular" and  building_id=3;  

select id,amount_paid,guest_status,check_in_date,default_rent,due_amount,last_bill_generation_date,
	next_dues_generation,paidtill,bill_generated_till,over_all_due ,security_deposit, 
    building_id,planned_check_out_date from guest where id="SLH001785" ;

call DUE_AMOUNT_COUNT(1);
call DUE_AMOUNT_COUNT_BY_BUILDING(3);


use buildingdb;
select id,guest_status,occupancy_type from guest;
select package_id from guest;
desc guest;
select * from guest where id="SLH002131";
SELECT * FROM GUEST WHERE email=
"ettiparida9@gmail.com";
select * from guest where first_name="Aditya kumar";
select package_id from guest where guest_status="active";
select * from guest where personal_number="9701389337";
select * FROM BEDS WHERE guest_id="SLH001415";
 select sum(amount_paid)  from payments where  guest_id="SLH000745" ;
 select due_amount from guest where guest_id="SLH000716";
 delete from guest where id IN('SLH001351','SLH001352','SLH001354','SLH001355','SLH001358','SLH001360','SLH001361');
 
 use buildingdb;
 select id,amount_paid,guest_status,check_in_date,default_rent,due_amount,last_bill_generation_date,
next_dues_generation,paidtill,bill_generated_till,over_all_due ,security_deposit, building_id from guest where id="SLH001644" ;

select  planned_check_out_date,id from guest where    occupancy_type IN ("OneMonth","daily");
use buildingdb ;

SET SQL_SAFE_UPDATES = 0;
update guest set last_bill_generation_date = check_in_date  where occupancy_type="Regular" and guest_status="Active";
update guest set due_amount = 0 ;
update guest set `bill_generated_till` = DATE_ADD(last_bill_generation_date , INTERVAL 30 DAY) where 
occupancy_type="Regular" and guest_status="Active"   ;
use buildingdb;
select * from beds where guest_id="SLH001203";
SELECT id,guest_status,sharing from guest where id="SLH001356" ;
select * from guest where default_rent=9000 and building_id=1 and occupancy_type="Regular";
# update Rates and sharing 
select id,sharing,building_id from guest where building_id=1 and guest_status="active";
select * from rates_config where room_type="NonAc" and occupancy_type="Regular" and building_id=1;
select * from rates_config;
UPDATE `buildingdb`.`guest` SET `planned_check_out_date` = null WHERE (`id` = 'SLH001644');