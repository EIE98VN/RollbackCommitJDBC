create table Customer(
	id varchar(10) not null primary key,
    name varchar(30) not null,
    age int,
    balance int
);
DELIMITER $$
CREATE PROCEDURE transfer(IN sender_id varchar(10), IN receiver_id varchar(10) , IN money INT)
BEGIN
	declare flag boolean default false;
    declare sender_balance int default (select balance from Customer where id = sender_id);

    declare continue handler for sqlexception set @flag = true;
    begin
    start transaction;
    set autocommit = false;
		If(sender_balance>money)
			Then
			Update Customer
			Set balance = balance - money
			Where id = sender_id;
        
			Update Customer
			Set balance = balance + money
			Where id = receiver_id;        
		end if;
        if flag then rollback;
        else commit;
        end if;
	end;	
END $$
DELIMITER ;

call transfer('C111','C22',12122121);