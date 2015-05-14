package workspace;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.Scanner;

public class bookshop {
    final public static boolean debug = true;
    // TODO AT LAST change the debug state
    private static boolean logged_in = false;
    private static boolean leave = false;

    private static String cname = "";
    private static int cid = 0;

    private static void Hello_world() {
        myfun.print_title();
        myfun.print_face();
    }
    private static void display_unlogged_in() {
        System.out.println("\tCurrently you are logged out.");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as a registered customer");
        System.out.println(" 2. Register a new account");
        System.out.println(" 3. Exit peacefully without hesitation");
    }

    private static void display_admin() {
        System.out.println("\tCurrently you are logged in as administrator "+cname+".");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as a customer");
        System.out.println(" 2. Log out");
        System.out.println(" 3. Change user\'s authority");
        System.out.println(" 4. Add new book");
        System.out.println(" 5. Add some copies to some book");
        System.out.println(" 6. Show statistics of this semester");
        System.out.println(" 7. Give user awards");
        System.out.println(" 8. Exit peacefully without hesitation");

    }

    private static void display_customer() {
        System.out.println("\tCurrently you are logged in as customer "+cname+".");
        System.out.println("\tWhat would you like to do next?");
        System.out.println(" 1. Log in as an administrator");
        System.out.println(" 2. Log out");
        System.out.println(" 3. Change my profile");
        System.out.println(" 4. Order some books");
        System.out.println(" 5. Feedback my books");
        System.out.println(" 6. Rate feedback");
        System.out.println(" 7. Book browse");
        System.out.println(" 8. Trust evaluation");
        System.out.println(" 9. Show feedback");
        System.out.println("10. Buying suggestion");
        System.out.println("11. Two degrees of separation");
        System.out.println("12. Exit peacefully without hesitation");
    }

    private static void print_split() {
        System.out.println();
        System.out.println("**********Wexley's Book Shop**********");
        System.out.println("**************************************");
        System.out.println();
    }
    public static void main(String[] args) {
        myconnector con = null;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    Scanner cin = new Scanner(new BufferedInputStream(System.in));

        if (!debug) Hello_world();
        try {
            con = new myconnector();

            System.out.println("Connection established XD!!");
            System.out.println("0v0 Welcome to Wexley's Bookshop!!!");
            print_split();

            ResultSet rs = null;
            boolean flag, flag1, flag2;

            while (!leave) {
                while (!logged_in) {
                    display_unlogged_in();
                    String choice = "";
                    int c = 0;
                    while ((choice = in.readLine()) == null || choice.length() == 0) ;
                    try {
                        c = Integer.parseInt(choice);
                    } catch (Exception e) {
                        continue;
                    }
                    if (c > 3 || c < 1) continue;
                    if (c == 1) {
                        System.out.println("Please Input your username:");
                        cname = in.readLine();
                        System.out.println("Please Input your password:");
                        String password = in.readLine();
                        try {
                            cid = mylogin.log_in(con.stmt, cname, password);
                        } catch (Exception e) {
                            System.out.println(">_< Log in process is broken, detailed information is below:");
                            if (debug) e.printStackTrace();
                            continue;
                        }
                        if (cid > 0) {
                            System.out.print("Log in succeed !! Now you are logged_in as " + cname);
                            System.out.println(", and your customer id is " + cid);
                            logged_in = true;
                            break;
                        } else {
                            System.out.println(">_< Log in failed, wrong username or password, please try again!");
                            continue;
                        }
                    } else if (c == 2) {
                        System.out.println("Please Input your username(3~30 characters)");
                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        do {
                            if (flag) System.out.println(">_< Sorry, you username has been picked !!");
                            if (flag1) System.out.println(">_< Sorry, your username is tooooo short !!");
                            if (flag2) System.out.println(">_< Sorry, your username is tooooo long !!");
                            flag = flag1 = flag2 = false;

                            cname = in.readLine();
                            if (cname.length() < 3) {
                                flag1 = true;
                                continue;
                            }
                            if (cname.length() > 30) {
                                flag2 = true;
                                continue;
                            }
                            try {
                                rs = con.stmt.executeQuery("select * from customer where login_name = \'" + cname + "\';");
                            } catch (Exception e) {
                                System.out.println(">_< Query username duplicate error at sql");
                                if (debug) e.printStackTrace();
                                break;
                            }
                            if (rs.next()) flag = true;
                        } while (flag1 || flag2 || flag);

                        String password, tmp, full_name, addr;
                        long phone = 0;

                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        System.out.println("Please Input your password(3~30 characters)");
                        do {
                            if (flag) System.out.println(">_< Different password input, please try again.");
                            flag = true;
                            do {
                                if (flag1) System.out.println("Password tooooooo weak!!! Please choose another!!");
                                if (flag2) System.out.println("Password tooooooo long!!! Please choose another!!");
                                password = in.readLine();
                                flag1 = password.length() < 3;
                                flag2 = password.length() > 30;
                            } while (flag1 || flag2);
                            System.out.println("Please Input your password again to confirm");
                            tmp = in.readLine();
                        } while (!tmp.equals(password));


                        do {
                            System.out.println("Please Input your full name(can be blank, 3-30 characters)");
                            full_name = in.readLine();
                            if (full_name.equals("")) break;
                        } while (full_name.length() < 3 || full_name.length() > 30);

                        do {
                            System.out.println("Please Input your address(can be blank)");
                            addr = in.readLine();
                            if (addr.equals("")) break;
                        } while (addr.length() < 3 || addr.length() > 30);

                        do {
                            System.out.println("Please Input your phone number(all numbers, 11 bits)");
                            tmp = in.readLine();
                            flag = false;
                            phone = 0;
                            for (int i = 0; i < tmp.length(); i++) {
                                if (tmp.charAt(i) < '0' || tmp.charAt(i) > '9') {
                                    flag = true;
                                    break;
                                }
                                phone = phone * 10l + (long) (tmp.charAt(i) - '0');
                            }
                        } while (tmp.length() != 11 || flag);

                        try {
                            myregister.register(con.stmt, cname, password, full_name, addr, phone);
                        } catch (Exception e) {
                            System.out.println(">_< Register failed, check the message for more");
                            e.printStackTrace();
                            continue;
                        }
                        System.out.println("0w0 Registered Successfully !! Please Log in now :)");
                        continue;
                    } else {
                        leave = true;
                        break;
                    }
                }
                if (!leave) {
                    print_split();
                    System.out.println("Now loading Book Management System...");
                    boolean admin = false;
                    String sql = "select admin from customer where cid = " + Integer.toString(cid) + " and admin = True;";
                    flag = false;
                    try {
                        rs = con.stmt.executeQuery(sql);
                    } catch (Exception e) {
                        System.out.println(">_< Query administration error at sql!");
                        if (debug) e.printStackTrace();
                        flag = true;
                    }
                    if (!flag && rs.next()) {
                        String tmp;
                        do {
                            System.out.println("Log in as administrator? [y/n]");
                            tmp = in.readLine();
                        } while (tmp.equals("") || (tmp.charAt(0) != 'y' && tmp.charAt(0) != 'n'));
                        admin = tmp.charAt(0) == 'y';
                    } else admin = false;

                    while (!leave && admin) {
                        print_split();
                        display_admin();
                        String choice = "";
                        int c = 0;
                        while ((choice = in.readLine()) == null || choice.length() == 0);
                        try {
                            c = Integer.parseInt(choice);
                        } catch (Exception e) {
                            continue;
                        }
                        if (c > 8 || c < 1) continue;
                        if (c == 1) {
                            admin = false; break;
                        } else if (c == 2) {
                            logged_in = admin = false; break;
                        } else if (c == 3) {
                            do {
                                System.out.println("Input the one's customer id or user name ? [c/u]");
                                choice = in.readLine();
                            } while (choice.equals("") || (choice.charAt(0) != 'c' && choice.charAt(0) != 'u'));

                            int k = 0; flag = false;

                            if (choice.charAt(0) == 'c') {
                                do {
                                    System.out.println("Please input the one's customer id");
                                    choice = in.readLine();
                                    try {
                                        k = Integer.parseInt(choice);
                                    } catch (Exception e) {
                                        System.out.println("Parse cid to integer error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    try {
                                        rs = con.stmt.executeQuery("select * from customer where cid = "
                                                + Integer.toString(k)+";");
                                    } catch (Exception e) {
                                        System.out.println(">_< Find the user with cid error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    if (rs.next()) break;
                                    do {
                                        System.out.println("NO such user, continue?[y/n]");
                                        choice = in.readLine();
                                    } while (choice.equals("") || (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'));
                                    if (choice.charAt(0) == 'y') continue; else {
                                        flag = true; break;
                                    }
                                } while (true);
                            } else {
                                do {
                                    System.out.println("Please input the one's user name");
                                    choice = in.readLine();
                                    try {
                                        rs = con.stmt.executeQuery(
                                                "select cid from customer where login_name = \'" +choice+"\';");
                                    } catch (Exception e) {
                                        System.out.println(">_< Find the user with user name error");
                                        if (debug) e.printStackTrace();
                                        continue;
                                    }
                                    if (rs.next()) {
                                        k = rs.getInt(1);
                                        break;
                                    } else {
                                        do {
                                            System.out.println("NO such user, continue?[y/n]");
                                            choice = in.readLine();
                                        } while (choice.equals("") || (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'));
                                        if (choice.charAt(0) == 'y') continue; else {
                                            flag = true; break;
                                        }
                                    }
                                } while (true);
                            }
                            if (flag) continue;
                            if (k == cid) {
                                System.out.println("You are not allowed to change your own authority.");
                                continue;
                            }

                            do {
                                System.out.println("\tYou can change in two ways");
                                System.out.println(" 1. Make the user customer");
                                System.out.println(" 2. Make the user administrator");
                                choice = in.readLine();
                            } while (choice.equals("") || (choice.charAt(0) != '1' && choice.charAt(0) != '2'));

                            sql = "update customer set admin =";
                            if (choice.charAt(0) == '2') sql += "true";
                                else sql += "false";
                            sql += " where cid = "+Integer.toString(k)+";";

                            try {
                                System.out.println(sql);
                                con.stmt.execute(sql);
                            } catch (Exception e) {
                                System.out.println(">_< change authority error");
                                if (debug) e.printStackTrace();
                                continue;
                            }
                            System.out.println("0w0 Changed authority successfully!!!");
                        } else if (c == 4) {

                        } else if (c == 5) {

                        } else if (c == 6) {

                        } else if (c == 7) {

                        } else {
                            leave = true; break;
                        }
                    }

                    while (!leave && logged_in) {
                        print_split();
                        display_customer();

                        System.out.println("Building...");
                        leave = true;
                        //TODO add customer feature.

                    }

                } //else choose to leave
            }
            System.out.println("Leaving.....................");
            print_split();
            System.out.println("My alipay account is zxybazh@qq.com");
            System.out.println("Buy me a cup of coffee please :)");
            System.out.println("Welcome and Goodbye~");
            cin.close();
            con.closeConnection();
        } catch (Exception e) {
        	 if (debug) e.printStackTrace();
        	 System.err.println (">_< Cannot connect to database server!!!");
        }
    }
}
