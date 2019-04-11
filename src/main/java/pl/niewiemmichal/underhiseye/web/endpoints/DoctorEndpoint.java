package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.model.Doctor;

@Controller
@RequestMapping("doctors")
public class DoctorEndpoint {

    @GetMapping("/{id}")
    @ResponseBody
    public String getDoctor(@PathVariable Integer id){
        return id.toString();
    }

    @GetMapping
    @ResponseBody
    public String getAllDoctors(){
        return "Get all doctors";
    }

    @PostMapping
    @ResponseBody
    public String addDoctor(){
        return "Add doctor";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public String updateDoctor(@PathVariable Integer id){
        return "Update doctor" + id.toString();
    }
}
