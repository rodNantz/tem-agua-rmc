package rodtwo.temagua.util;

import java.util.List;

import rodtwo.temagua.services.bean.PeriodoRodizio;

public class RodizioListUtil {
	
	public static String convertRodizioListToString(List<PeriodoRodizio> rodiziolist, String lineBreak) {
		StringBuilder rodStr = new StringBuilder();
		String doubleLineBreak = lineBreak + lineBreak;
		
		for(PeriodoRodizio r : rodiziolist) {
			rodStr.append( "Início do rodízio: "+ r.hrInicio + lineBreak );
			rodStr.append( "Previsão de volta da água: "+ r.hrFim + doubleLineBreak );
		}
		if (rodiziolist.isEmpty())
			rodStr.append( "Grupo não encontrado." );
		
		return rodStr.toString();
	}

}
