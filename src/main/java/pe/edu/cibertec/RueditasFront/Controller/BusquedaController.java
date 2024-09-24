package pe.edu.cibertec.RueditasFront.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.RueditasFront.ViewModel.BusquedaModel;
import pe.edu.cibertec.RueditasFront.dto.BusquedaRequestDTO;
import pe.edu.cibertec.RueditasFront.dto.BusquedaResponseDTO;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

@Autowired
RestTemplate restTemplate;

@GetMapping("/inicio")
public String inicio(Model model){

    BusquedaModel busquedaModel=new BusquedaModel("00","","");
    model.addAttribute("busquedaModel",busquedaModel);


    return "inicio";
}



@PostMapping("/identificar")
public String identificar(@RequestParam("placa")String placa,Model model){


   if (placa ==null || placa.trim().length()==0){

       BusquedaModel busquedaModel=new BusquedaModel("01","Debe Ingresar una Placa Correcta","");
       model.addAttribute("busquedaModel",busquedaModel);
       return "inicio";
   }

    // Validar si la placa es una de las dos permitidas
    if (!placa.equals("ASR-121") && !placa.equals("FAD-124")) {
        BusquedaModel busquedaModel = new BusquedaModel("02", "Debe Ingresar una Placa Correcta", "");
        model.addAttribute("busquedaModel", busquedaModel);
        return "inicio";
    }

    //////////////
   try {

       String endpoint="http://localhost:8081/encontrar/datos";

       BusquedaRequestDTO busquedaRequestDTO=new BusquedaRequestDTO(placa);
       BusquedaResponseDTO busquedaResponseDTO=restTemplate.postForObject(endpoint,busquedaRequestDTO, BusquedaResponseDTO.class);

       //VALIDAR

       //if(busquedaRequestDTO.Placa().equals("ASR-121")){


       if(busquedaRequestDTO!=null){

         //  BusquedaModel busquedaModel=new BusquedaModel("00","",busquedaResponseDTO.Marca());
          // BusquedaModel busquedaModel=new BusquedaModel("00",busquedaResponseDTO.Precio(),busquedaResponseDTO.Color());
          // model.addAttribute("busquedaModel",busquedaModel);
           //return "principal";



           BusquedaResponseDTO busquedaResponseDTO1=new BusquedaResponseDTO(busquedaResponseDTO.Marca(),busquedaResponseDTO.Modelo(),
                   busquedaResponseDTO.NroAsiento(),busquedaResponseDTO.Precio(), busquedaResponseDTO.Color());
           model.addAttribute("busquetaDTO",busquedaResponseDTO1);

           return "principal";








       } else{
           BusquedaModel busquedaModel=new BusquedaModel("02","Error Nombre de la Placa no Identificadaq ","");
           model.addAttribute("busquedaModel",busquedaModel);
           return "inicio";
       }

   }
   catch (Exception e){


       BusquedaModel busquedaModel=new BusquedaModel("99","Debe Ingresar una Placa Correcta","");
       model.addAttribute("busquedaModel",busquedaModel);

       System.out.println(e.getMessage());

       return "inicio";

   }




}


}
