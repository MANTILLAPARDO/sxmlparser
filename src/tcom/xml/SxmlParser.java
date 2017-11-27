package tcom.xml;

import static tcom.xml.WellformedXmlLexer.ANGD;
import static tcom.xml.WellformedXmlLexer.ANGI;
import static tcom.xml.WellformedXmlLexer.EOF;
import static tcom.xml.WellformedXmlLexer.EQ;
import static tcom.xml.WellformedXmlLexer.ID;
import static tcom.xml.WellformedXmlLexer.SLASH;
import static tcom.xml.WellformedXmlLexer.STRING;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.runtime.ANTLRInputStream;

/**
 * Parser para el lenguaje Sxml
 * @author Juan Camilo Mantilla - Felipe Pardo
 *
 */
public class SxmlParser extends BasicParser{
	
	/**
	 * Conecta el parser con el analizador lexico (clase WellFormedXmlLexer)
	 * @param archName Nombre del archivo para analizar
	 * @throws XmlParsingException Si se detecta cualquier error
	 */
	public void analizar(String archName) throws XmlParsingException{
		InputStream is = SxmlParser.class.getResourceAsStream("/" + archName);
		try {
			ANTLRInputStream in = new ANTLRInputStream(is);

			lexer = new WellformedXmlLexer(in);

			parse();

		} catch (IllegalArgumentException e) {
			throw new XmlParsingException("Caracter invalido en la entrada");
		} catch (IOException e) {
			throw new XmlParsingException("Complicacion de I/O : "+e.getMessage());
		} catch (XmlParsingException e){
			StringBuilder sb = new StringBuilder();
			String eol = System.getProperty("line.separator");
			
			sb.append(e.getMessage());
			sb.append(eol);
			sb.append(eol);
			
			throw new XmlParsingException(sb.toString());

		}

	}
	
	/**
	 * Inicio del proceso de parsing
	 * @throws XmlParsingException
	 */
	private void parse() throws XmlParsingException {
		
		sigToken();
		PelementoPrincipal();
		try{
			expect(EOF);
		}catch (XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		/**
		while (token.getType()!=EOF) {
			System.out.println(tokenName(token.getType())+" "+token.getText());
			sigToken();
		}**/
		
	}

	private void PelementoPrincipal() throws XmlParsingException  {

		System.out.println(tokenName(token.getType())+" "+token.getText());
		try{
			expect(ANGI);
		}catch(XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		System.out.println(tokenName(token.getType())+" "+token.getText());
		Pelemento();
	}
	
	private void Pelemento() throws XmlParsingException {
		try{
			expect(ID);
		}catch(XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		System.out.println(tokenName(token.getType())+" "+token.getText());
		while (token.getType()== ID){
			Patribute();
		}
		if (token.getType()==SLASH){
			sigToken();
			System.out.println(tokenName(token.getType())+" "+token.getText());
			try{
				expect(ANGD);
			}catch(XmlParsingException e){
				throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
			}
			System.out.println(tokenName(token.getType())+" "+token.getText());
		}else if (token.getType()==ANGD){
			//System.out.println(tokenName(token.getType())+" "+token.getText());
			sigToken();
			System.out.println(tokenName(token.getType())+" "+token.getText());
			try{
				expect(ANGI);
			}catch(XmlParsingException e){
				throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
			}
			System.out.println(tokenName(token.getType())+" "+token.getText());
			PelementoPrima();
		}else{
			throw new XmlParsingException("Pelemento else "+ token.getText());
		}
		

	}

	private void PelementoPrima() throws XmlParsingException {
		try{
			while (token.getType()!= SLASH){
				Pelemento();
				System.out.println(tokenName(token.getType())+" "+token.getText());
				try{
					expect(ANGI);
				}catch(XmlParsingException e){
					throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
				}
				System.out.println(tokenName(token.getType())+" "+token.getText());
			}
			sigToken();
			System.out.println(tokenName(token.getType())+" "+token.getText());
			try{
				expect(ID);
			}catch(XmlParsingException e){
				throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
			}
			System.out.println(tokenName(token.getType())+" "+token.getText());
			try{
				expect(ANGD);
			}catch(XmlParsingException e){
				throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
			}
			System.out.println(tokenName(token.getType())+" "+token.getText());
		}catch (XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		
	}

	private void Patribute()throws XmlParsingException {
		try {
			expect(ID);
		}catch (XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		try{
			expect(EQ);
		}catch (XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}
		try{
			expect(STRING);
		}catch (XmlParsingException e){
			throw new XmlParsingException("Elemento invalido "+ token.getText()+" ubicado en "+ locateError(token));
		}

		
	}
}
