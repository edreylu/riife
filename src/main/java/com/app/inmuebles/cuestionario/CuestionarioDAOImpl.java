/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.inmuebles.cuestionario;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author usuario
 */
@Repository
public class CuestionarioDAOImpl implements CuestionarioDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List lista = null;
    private String sql;

    @Override
    public List<Cuestionario> getRecords() {
        sql = "select cu.idcuestionario, "
                + "cu.cuestionario, "
                + "cu.fecharegistro, "
                + "cu.nousuarioregistro, "
                + "(select us.nombre||' '||us.apellido_paterno||' '||us.apellido_materno from usuarios us where us.no_usuario = cu.nousuarioregistro) nomUsuarioRegistro,"
                + "cu.fechamodif, "
                + "cu.nousuariomodif, "
                + "(select us.nombre||' '||us.apellido_paterno||' '||us.apellido_materno from usuarios us where us.no_usuario = cu.nousuariomodif) nomUsuarioModif, "
                + "cu.idestatus "
                + "from cuestionario cu\n"
                + "               order by 1";
        try {
            lista = jdbcTemplate.query(sql, new CuestionarioRowMapper());
        } catch (DataAccessException e) {
            System.err.print(e);
        }
        return lista;
    }

    @Override
    public int addCuestionario(Cuestionario cu) {
        int valor = 0;
        int idCuestionario = jdbcTemplate.queryForObject("select nvl(max(idcuestionario),0)+ 1 from cuestionario", Integer.class);
        cu.setIdCuestionario(idCuestionario);
        sql = "INSERT INTO cuestionario (idcuestionario, cuestionario, fecharegistro, nousuarioregistro, fechamodif, nousuariomodif, "
                + "    idestatus) "
                + "VALUES (?,?,SYSDATE,?,?,?,?)";
        try {
            valor = jdbcTemplate.update(sql, cu.getIdCuestionario(),
                    cu.getCuestionario().toUpperCase(),
                    cu.getUsuarioRegistro().getNoUsuario(),
                    null,
                    null,
                    1);
        } catch (DataAccessException e) {
            System.err.print(e);
        }
        return valor;
    }

    @Override
    public int editCuestionario(Cuestionario cu) {
        int valor = 0;
        sql = "UPDATE CUESTIONARIO SET cuestionario = ?, fechamodif = SYSDATE, nousuariomodif = ? WHERE idcuestionario = ? ";
        try {
            valor = jdbcTemplate.update(sql, cu.getCuestionario().toUpperCase(),
                    cu.getUsuarioModif().getNoUsuario(),
                    cu.getIdCuestionario());
        } catch (DataAccessException e) {
            System.err.print(e);
        }
        return valor;
    }

    @Override
    public Cuestionario getCuestionario(int id) {

        Cuestionario cu = null;
        sql = "select cu.idcuestionario, "
                + "cu.cuestionario, "
                + "cu.fecharegistro, "
                + "cu.nousuarioregistro, "
                + "(select us.nombre||' '||us.apellido_paterno||' '||us.apellido_materno from usuarios us where us.no_usuario = cu.nousuarioregistro) nomUsuarioRegistro,"
                + "cu.fechamodif, "
                + "cu.nousuariomodif, "
                + "(select us.nombre||' '||us.apellido_paterno||' '||us.apellido_materno from usuarios us where us.no_usuario = cu.nousuariomodif) nomUsuarioModif, "
                + "cu.idestatus "
                + "from cuestionario cu WHERE cu.idcuestionario =" + id;
        try {
            cu = jdbcTemplate.queryForObject(sql, new CuestionarioRowMapper());
        } catch (DataAccessException e) {
            System.err.print(e);
        }
        return cu;
    }

    @Override
    public int deleteCuestionario(int id, int opcion) {
        int valor = 0;
        sql = "UPDATE CUESTIONARIO SET IDESTATUS= ? where idcuestionario = ?";
        try {
            valor = jdbcTemplate.update(sql, opcion, id);
        } catch (DataAccessException e) {
            System.err.print(e);
        }
        return valor;
    }

}
