package org.example.controller;

import jakarta.validation.Valid;
import org.example.domain.*;
import org.example.dto.*;
import org.example.service.CustomerService;
import org.example.service.EmployeeService;
import org.example.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.example.service.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private org.example.service.AppointmentService appointmentService;

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final UserActivityService userActivityService;

    public AuthController(CustomerService customerService, EmployeeService employeeService, PasswordEncoder passwordEncoder,UserActivityService userActivityService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.userActivityService = userActivityService;
    }

    /* Home Page
    @GetMapping("/")
    public String home() {
        return "home";
    }*/

    // Customer Registration
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (customerService.findByUsername(customer.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.username.taken", "Username is already taken");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("customer", customer);
            return "register";
        }

        customerService.registerCustomer(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Owner: with validation
    @GetMapping("/owner/create-employee")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("roles", Role.values());
        return "create-employee";
    }

    @PostMapping("/owner/create-employee")
    public String createEmployee(@Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (employeeService.findByUsername(employee.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.username.taken", "Username is already taken");
        }

        if (employee.getWorkStartTime() != null && employee.getWorkEndTime() != null) {
            if (!employee.getWorkEndTime().isAfter(employee.getWorkStartTime())) {
                bindingResult.rejectValue("workEndTime", "error.time.invalid", "End time must be after start time");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employee);
            model.addAttribute("roles", Role.values());
            return "create-employee";
        }

        employeeService.createEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee account created successfully!");
        return "redirect:/owner/dashboard";
    }

    // Owner Dashboard
    @GetMapping("/owner/dashboard")
    public String ownerDashboard(Model model,
                                 @RequestParam(value = "search", required = false) String search) {

        List<Employee> employees;
        List<Customer> customers;

        if (search != null && !search.trim().isEmpty()) {
            String trimmedSearch = search.trim().toLowerCase();
            employees = employeeService.searchByKeyword(trimmedSearch);
            customers = customerService.searchByKeyword(trimmedSearch);
        } else {
            employees = employeeService.findAll();
            customers = customerService.findAll();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("customers", customers);
        model.addAttribute("search", search);

        return "owner-dashboard";
    }
    // UM13 - Owner views user activity
    @GetMapping("/owner/user-activity") //sprint3
    public String viewUserActivity(Model model,
                                   @RequestParam(value = "filter", required = false, defaultValue = "all") String filter) {

        List<UserActivity> activities;

        if ("staff".equalsIgnoreCase(filter)) {
            activities = userActivityService.getStaffActivities();
            model.addAttribute("filterType", "Staff");
        } else if ("customer".equalsIgnoreCase(filter)) {
            activities = userActivityService.getCustomerActivities();
            model.addAttribute("filterType", "Customer");
        } else {
            activities = userActivityService.getAllActivities();
            model.addAttribute("filterType", "All Users");
        }

        model.addAttribute("activities", activities);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("activityCount", activities.size());

        return "owner-user-activity";
    }

    // Customer Dashboard & Profile
    @GetMapping("/customer/dashboard")
    public String customerDashboard() {
        return "customer-dashboard";
    }

    @GetMapping("/customer/profile")
    public String customerProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.findByUsername(username);
        if (customer == null) {
            return "redirect:/login?error";
        }
        model.addAttribute("customer", customer);
        return "customer-profile";
    }

    @GetMapping("/owner/customers")
    public String viewCustomers(Model model) {
        // TODO: get customer list from service
        return "owner-customers";
    }

    // Show edit profile form
    @GetMapping("/customer/profile/edit")
    public String editCustomerProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.findByUsername(username);
        if (customer == null) {
            return "redirect:/login?error";
        }
        // Create DTO and copy data from entity
        CustomerEdit edit = new CustomerEdit();
        edit.setName(customer.getName());
        edit.setEmail(customer.getEmail());
        edit.setPhoneNumber(customer.getPhoneNumber());
        model.addAttribute("customer", edit);
        return "customer-profile-edit";
    }

    // Process profile update
    @PostMapping("/customer/profile/edit")
    public String updateCustomerProfile(@Valid @ModelAttribute("customer") CustomerEdit edit,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "customer-profile-edit";
        }

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer existing = customerService.findByUsername(currentUsername);

        if (existing != null) {
            existing.setName(edit.getName());
            existing.setEmail(edit.getEmail());
            existing.setPhoneNumber(edit.getPhoneNumber());
            customerService.updateCustomer(existing);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/customer/profile";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile.");
        return "redirect:/customer/profile";
    }

    // Activate Employee
    @PostMapping("/owner/employee/activate/{id}")
    public String activateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.activateEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee activated successfully!");
        return "redirect:/owner/dashboard";
    }

    // Deactivate Employee
    @PostMapping("/owner/employee/deactivate/{id}")
    public String deactivateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deactivateEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deactivated successfully!");
        return "redirect:/owner/dashboard";
    }

    // Activate Customer
    @PostMapping("/owner/customer/activate/{id}")
    public String activateCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.activateCustomer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Customer activated successfully!");
        return "redirect:/owner/dashboard";
    }

    // Deactivate Customer
    @PostMapping("/owner/customer/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.deactivateCustomer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Customer deactivated successfully!");
        return "redirect:/owner/dashboard";
    }

    // Updated Staff Dashboard Route
    @GetMapping("/staff/dashboard")
    public String staffDashboard(Model model, Authentication authentication) {
        // Cast the principal to your CustomUserDetails to get the ID
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long staffId = userDetails.getEmployeeId();

        // Fetch dynamic stats from the service
        long todayTasks = appointmentService.getTodayTaskCount(staffId);
        long totalServices = appointmentService.getCompletedServicesCount(staffId);

        // Add attributes for Thymeleaf
        model.addAttribute("todayTasks", String.format("%02d", todayTasks));
        model.addAttribute("totalServices", totalServices);

        return "staff-dashboard";
    }

    // Staff Profile View
    @GetMapping("/staff/profile")
    public String staffProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return "redirect:/login?error";
        }
        model.addAttribute("employee", employee);
        return "staff-profile";
    }

    // Show Staff Edit Profile Form
    @GetMapping("/staff/profile/edit")
    public String editStaffProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return "redirect:/login?error";
        }

        EmployeeEdit edit = new EmployeeEdit();
        edit.setFirstName(employee.getFirstName());
        edit.setLastName(employee.getLastName());
        edit.setEmail(employee.getEmail());

        model.addAttribute("employee", edit);
        return "staff-profile-edit";
    }

    // Process Staff Profile Update
    @PostMapping("/staff/profile/edit")
    public String updateStaffProfile(@Valid @ModelAttribute("employee") EmployeeEdit edit,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "staff-profile-edit";
        }

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee existing = employeeService.findByUsername(currentUsername);

        if (existing != null) {
            existing.setFirstName(edit.getFirstName());
            existing.setLastName(edit.getLastName());
            existing.setEmail(edit.getEmail());

            employeeService.updateEmployee(existing);

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/staff/profile";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile.");
        return "redirect:/staff/profile";
    }
    // Show Staff Change Password Form
    @GetMapping("/staff/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordForm", new EmployeeChangePassword());
        return "staff-change-password";
    }

    // Process Staff Password Change
    @PostMapping("/staff/change-password")
    public String changeStaffPassword(@Valid @ModelAttribute("passwordForm") EmployeeChangePassword form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "staff-change-password";
        }

        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "error.confirm", "New passwords do not match");
            return "staff-change-password";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);

        if (employee == null || !passwordEncoder.matches(form.getCurrentPassword(), employee.getPassword())) {
            bindingResult.rejectValue("currentPassword", "error.current", "Current password is incorrect");
            return "staff-change-password";
        }

        // Update password
        employee.setPassword(passwordEncoder.encode(form.getNewPassword()));
        employeeService.updateEmployee(employee);

        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/staff/profile";
    }

    // Show Customer Reset Password Form
    @GetMapping("/customer/reset-password") //sprint3
    public String showResetPasswordForm(Model model) {
        model.addAttribute("resetForm", new CustomerResetPassword());
        return "customer-reset-password";
    }

    // Process Customer Password Reset
    @PostMapping("/customer/reset-password")
    public String resetCustomerPassword(@Valid @ModelAttribute("resetForm") CustomerResetPassword form,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "customer-reset-password";
        }

        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "error.confirm", "Passwords do not match");
            return "customer-reset-password";
        }

        Customer customer = customerService.findByUsername(form.getUsername());
        if (customer == null) {
            bindingResult.rejectValue("username", "error.notfound", "Username not found");
            return "customer-reset-password";
        }

        // Update password
        customer.setPassword(passwordEncoder.encode(form.getNewPassword()));
        customerService.updateCustomer(customer);

        redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully! Please login with your new password.");
        return "redirect:/login";
    }

    // Receptionist: View Staff Roles & Availability
    @GetMapping("/reception/dashboard")
    public String receptionistDashboard() {
        return "receptionist-dashboard";
    }
    @GetMapping("/reception/profile")
    public String receptionistProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return "redirect:/login?error";
        }
        model.addAttribute("employee", employee);
        return "receptionist-profile";
    }
    //RECEPTIONIST PROFILE & EDIT

    // Show Receptionist Edit Profile Form
    @GetMapping("/reception/profile/edit")
    public String editReceptionistProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return "redirect:/login?error";
        }
        EmployeeEdit edit = new EmployeeEdit();
        edit.setFirstName(employee.getFirstName());
        edit.setLastName(employee.getLastName());
        edit.setEmail(employee.getEmail());
        model.addAttribute("employee", edit);
        return "receptionist-profile-edit";
    }

    // Process Receptionist Profile Update
    @PostMapping("/reception/profile/edit")
    public String updateReceptionistProfile(@Valid @ModelAttribute("employee") EmployeeEdit edit,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "receptionist-profile-edit";
        }
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee existing = employeeService.findByUsername(currentUsername);
        if (existing != null) {
            existing.setFirstName(edit.getFirstName());
            existing.setLastName(edit.getLastName());
            existing.setEmail(edit.getEmail());
            employeeService.updateEmployee(existing);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/reception/profile";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile.");
        return "redirect:/reception/profile";
    }

    // Show Receptionist Change Password Form
    @GetMapping("/reception/change-password")
    public String showReceptionistChangePasswordForm(Model model) {
        model.addAttribute("passwordForm", new EmployeeChangePassword());
        return "receptionist-change-password";
    }

    // Process Receptionist Password Change
    @PostMapping("/reception/change-password")
    public String changeReceptionistPassword(@Valid @ModelAttribute("passwordForm") EmployeeChangePassword form,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "receptionist-change-password";
        }
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "error.confirm", "New passwords do not match");
            return "receptionist-change-password";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByUsername(username);
        if (employee == null || !passwordEncoder.matches(form.getCurrentPassword(), employee.getPassword())) {
            bindingResult.rejectValue("currentPassword", "error.current", "Current password is incorrect");
            return "receptionist-change-password";
        }
        // Update password
        employee.setPassword(passwordEncoder.encode(form.getNewPassword()));
        employeeService.updateEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/reception/profile";
    }
    @GetMapping("/reception/staff-view") //sprint3
    public String viewStaffForReceptionist(Model model) {
        List<Employee> staffList = employeeService.findAll();
        model.addAttribute("staffList", staffList);
        return "receptionist-staff-view";
    }

    // --- API ENDPOINT FOR BOOKING DROPDOWN ---
    @GetMapping("/api/employees/staff-list")
    @ResponseBody
    public List<Map<String, Object>> getStaffList() {
        return employeeService.findAll().stream()
                .filter(emp -> emp.getRole() == Role.STAFF && "ACTIVE".equals(emp.getAccStatus()))
                .map(emp -> {
                    // We use a Map so we don't expose passwords or sensitive employee data to the frontend
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", emp.getEmployeeId());
                    map.put("firstName", emp.getFirstName());
                    map.put("lastName", emp.getLastName());
                    return map;
                })
                .collect(Collectors.toList());
    }
}