// $ANTLR 2.7.4: "XQueryTree.g" -> "XQueryTreeParser.java"$

	package org.exist.xquery.parser;

	import antlr.debug.misc.*;
	import java.io.StringReader;
	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Iterator;
	import java.util.Stack;
	import org.exist.storage.BrokerPool;
	import org.exist.storage.DBBroker;
	import org.exist.storage.analysis.Tokenizer;
	import org.exist.EXistException;
	import org.exist.dom.DocumentSet;
	import org.exist.dom.DocumentImpl;
	import org.exist.dom.QName;
	import org.exist.security.PermissionDeniedException;
	import org.exist.security.User;
	import org.exist.xquery.*;
	import org.exist.xquery.value.*;
	import org.exist.xquery.functions.*;

public interface XQueryTreeParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int QNAME = 4;
	int PREDICATE = 5;
	int FLWOR = 6;
	int PARENTHESIZED = 7;
	int ABSOLUTE_SLASH = 8;
	int ABSOLUTE_DSLASH = 9;
	int WILDCARD = 10;
	int PREFIX_WILDCARD = 11;
	int FUNCTION = 12;
	int UNARY_MINUS = 13;
	int UNARY_PLUS = 14;
	int XPOINTER = 15;
	int XPOINTER_ID = 16;
	int VARIABLE_REF = 17;
	int VARIABLE_BINDING = 18;
	int ELEMENT = 19;
	int ATTRIBUTE = 20;
	int ATTRIBUTE_CONTENT = 21;
	int TEXT = 22;
	int VERSION_DECL = 23;
	int NAMESPACE_DECL = 24;
	int DEF_NAMESPACE_DECL = 25;
	int DEF_COLLATION_DECL = 26;
	int DEF_FUNCTION_NS_DECL = 27;
	int GLOBAL_VAR = 28;
	int FUNCTION_DECL = 29;
	int PROLOG = 30;
	int ATOMIC_TYPE = 31;
	int MODULE = 32;
	int ORDER_BY = 33;
	int POSITIONAL_VAR = 34;
	int BEFORE = 35;
	int AFTER = 36;
	int MODULE_DECL = 37;
	int ATTRIBUTE_TEST = 38;
	int COMP_ELEM_CONSTRUCTOR = 39;
	int COMP_ATTR_CONSTRUCTOR = 40;
	int COMP_TEXT_CONSTRUCTOR = 41;
	int COMP_COMMENT_CONSTRUCTOR = 42;
	int COMP_PI_CONSTRUCTOR = 43;
	int COMP_NS_CONSTRUCTOR = 44;
	int COMP_DOC_CONSTRUCTOR = 45;
	int LITERAL_xpointer = 46;
	int LPAREN = 47;
	int RPAREN = 48;
	int NCNAME = 49;
	int LITERAL_xquery = 50;
	int LITERAL_version = 51;
	int SEMICOLON = 52;
	int LITERAL_module = 53;
	int LITERAL_namespace = 54;
	int EQ = 55;
	int STRING_LITERAL = 56;
	int LITERAL_import = 57;
	int LITERAL_declare = 58;
	int LITERAL_default = 59;
	int LITERAL_xmlspace = 60;
	int LITERAL_ordering = 61;
	int LITERAL_construction = 62;
	// "base-uri" = 63
	int LITERAL_function = 64;
	int LITERAL_variable = 65;
	int LITERAL_encoding = 66;
	int LITERAL_collation = 67;
	int LITERAL_element = 68;
	int LITERAL_preserve = 69;
	int LITERAL_strip = 70;
	int LITERAL_ordered = 71;
	int LITERAL_unordered = 72;
	int DOLLAR = 73;
	int LCURLY = 74;
	int RCURLY = 75;
	int LITERAL_at = 76;
	int LITERAL_external = 77;
	int LITERAL_as = 78;
	int COMMA = 79;
	int LITERAL_empty = 80;
	int QUESTION = 81;
	int STAR = 82;
	int PLUS = 83;
	int LITERAL_item = 84;
	int LITERAL_for = 85;
	int LITERAL_let = 86;
	int LITERAL_some = 87;
	int LITERAL_every = 88;
	int LITERAL_if = 89;
	int LITERAL_where = 90;
	int LITERAL_return = 91;
	int LITERAL_in = 92;
	int COLON = 93;
	int LITERAL_order = 94;
	int LITERAL_by = 95;
	int LITERAL_ascending = 96;
	int LITERAL_descending = 97;
	int LITERAL_greatest = 98;
	int LITERAL_least = 99;
	int LITERAL_satisfies = 100;
	int LITERAL_typeswitch = 101;
	int LITERAL_case = 102;
	int LITERAL_then = 103;
	int LITERAL_else = 104;
	int LITERAL_or = 105;
	int LITERAL_and = 106;
	int LITERAL_instance = 107;
	int LITERAL_of = 108;
	int LITERAL_castable = 109;
	int LITERAL_cast = 110;
	int LT = 111;
	int GT = 112;
	int LITERAL_eq = 113;
	int LITERAL_ne = 114;
	int LITERAL_lt = 115;
	int LITERAL_le = 116;
	int LITERAL_gt = 117;
	int LITERAL_ge = 118;
	int NEQ = 119;
	int GTEQ = 120;
	int LTEQ = 121;
	int LITERAL_is = 122;
	int LITERAL_isnot = 123;
	int ANDEQ = 124;
	int OREQ = 125;
	int LITERAL_to = 126;
	int MINUS = 127;
	int LITERAL_div = 128;
	int LITERAL_idiv = 129;
	int LITERAL_mod = 130;
	int LITERAL_union = 131;
	int UNION = 132;
	int LITERAL_intersect = 133;
	int LITERAL_except = 134;
	int SLASH = 135;
	int DSLASH = 136;
	int LITERAL_text = 137;
	int LITERAL_node = 138;
	int LITERAL_attribute = 139;
	int LITERAL_comment = 140;
	// "processing-instruction" = 141
	// "document-node" = 142
	int LITERAL_document = 143;
	int SELF = 144;
	int XML_COMMENT = 145;
	int XML_PI = 146;
	int LPPAREN = 147;
	int RPPAREN = 148;
	int AT = 149;
	int PARENT = 150;
	int LITERAL_child = 151;
	int LITERAL_self = 152;
	int LITERAL_descendant = 153;
	// "descendant-or-self" = 154
	// "following-sibling" = 155
	int LITERAL_following = 156;
	int LITERAL_parent = 157;
	int LITERAL_ancestor = 158;
	// "ancestor-or-self" = 159
	// "preceding-sibling" = 160
	int DOUBLE_LITERAL = 161;
	int DECIMAL_LITERAL = 162;
	int INTEGER_LITERAL = 163;
	int END_TAG_START = 164;
	int QUOT = 165;
	int APOS = 166;
	int QUOT_ATTRIBUTE_CONTENT = 167;
	int APOS_ATTRIBUTE_CONTENT = 168;
	int ELEMENT_CONTENT = 169;
	int XML_COMMENT_END = 170;
	int XML_PI_END = 171;
	int XML_CDATA = 172;
	int LITERAL_collection = 173;
	int LITERAL_preceding = 174;
	int XML_PI_START = 175;
	int XML_CDATA_START = 176;
	int XML_CDATA_END = 177;
	int LETTER = 178;
	int DIGITS = 179;
	int HEX_DIGITS = 180;
	int NMSTART = 181;
	int NMCHAR = 182;
	int WS = 183;
	int EXPR_COMMENT = 184;
	int PRAGMA = 185;
	int PRAGMA_CONTENT = 186;
	int PRAGMA_QNAME = 187;
	int PREDEFINED_ENTITY_REF = 188;
	int CHAR_REF = 189;
	int NEXT_TOKEN = 190;
	int CHAR = 191;
	int BASECHAR = 192;
	int IDEOGRAPHIC = 193;
	int COMBINING_CHAR = 194;
	int DIGIT = 195;
	int EXTENDER = 196;
}
