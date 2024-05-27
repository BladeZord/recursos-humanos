package gm.RRHH.controller;

import gm.RRHH.exception.RecursoNoEncontradoException;
import gm.RRHH.model.Empleado;
import gm.RRHH.services.EmpleadoService;
import gm.RRHH.services.IEmpleadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
// http://localhost:8080/rrhh-app/v1/es/
@RequestMapping("rrhh-app/v1/es/")
@CrossOrigin(value = "http://localhost:3000")
public class EmpleadoController {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoController.class);

    @Autowired
    private IEmpleadoService empleadoService;

    //http://localhost:8080/rrhh-app/v1/es/empleados
    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados(){
        var empleados = empleadoService.listarEmpleados();

        empleados.forEach((empleado -> logger.info(empleado.toString())));

        return empleados;
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadPorId(@PathVariable Integer id){
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if(empleado == null)
            throw new RecursoNoEncontradoException("No se encontró el empleado con el id: " + id);
        return ResponseEntity.ok(empleado);
    }

    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado){
        logger.info("Empleado a agregar: " + empleado);

        return empleadoService.guardarEmpleado(empleado);
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable int id,
                                                       @RequestBody Empleado empleadoRecibido){
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if(empleado == null)
            throw new RecursoNoEncontradoException("El id recibido no existe: " + id);
        empleado.setNombre(empleadoRecibido.getNombre());
        empleado.setDepartamento(empleadoRecibido.getDepartamento());
        empleado.setSueldo(empleadoRecibido.getSueldo());

        empleadoService.guardarEmpleado(empleado);
        return ResponseEntity.ok(empleado);
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Integer id){
        Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
        if(empleado == null)
            throw new RecursoNoEncontradoException("El id recibido no existe: " + id);
        empleadoService.eliminarEmpleado(empleado);

        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);

        return ResponseEntity.ok(respuesta);
    }

}
