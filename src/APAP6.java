import java.util.Vector;

public class APAP6 {
    public static void main(String[] kbIn){
        Database DB = new Database();
    //----------------Usuarios---------------------
        String[] user1=DB.getDBUsers()[0];
        String[] user2=DB.getDBUsers()[1];

        System.out.println(user1[0]+","+user1[1]);
        System.out.println(user2[0]+","+user2[1]);
    //------------------Materiales---------------------
        Vector materials = DB.getDBMaterials();
        for(int i=0; i<materials.size();i++){
            Vector mat= (Vector) materials.get(i);
            for(int j=0; j<mat.size();j++){
                System.out.print(mat.get(j)+", ");
            }
            System.out.println();
        }
    //-----------Checar si un profe existe-------------
        String num="123456789";
        if(DB.matchTrabNum(num)) {
            String[] nom = DB.getDBprofessorData(num);
            System.out.println("Profesor: " + nom[0] + " " + nom[1] + " " + nom[2]);
        }
        else
            System.out.println("No Existe");
    }
}
