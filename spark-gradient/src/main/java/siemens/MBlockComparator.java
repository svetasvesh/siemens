package siemens;

import java.util.*;
import java.io.Serializable;

public class MBlockComparator implements Serializable, Comparator<MBlock> {
		public int compare (MBlock entry1, MBlock entry2) {
			int result = entry1.compareTo(entry2);
			return result;
		}	
	};