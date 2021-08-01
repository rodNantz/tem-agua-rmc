package rodtwo.temagua.util;

import java.util.List;

import rodtwo.temagua.services.bean.PeriodoRodizio;

public class RodizioListUtil {
	
	public static String convertRodizioListToString(List<PeriodoRodizio> rodiziolist, String lineBreak) {
		StringBuilder rodStr = new StringBuilder();
		String doubleLineBreak = lineBreak + lineBreak;
		
		for(PeriodoRodizio r : rodiziolist) {
			rodStr.append( "In�cio do rod�zio: "+ r.hrInicio + lineBreak );
			rodStr.append( "Previs�o de volta da �gua: "+ r.hrFim + doubleLineBreak );
		}
		if (rodiziolist.isEmpty())
			rodStr.append( "Grupo n�o encontrado." );
		
		return rodStr.toString();
	}

}
