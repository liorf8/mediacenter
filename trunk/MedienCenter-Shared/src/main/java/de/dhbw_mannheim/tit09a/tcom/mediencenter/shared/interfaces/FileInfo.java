package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

/**
 * Informationen zu einer bestimmten Datei.
 * 
 * @author mhertram
 * 
 */
public abstract class FileInfo
{
	/**
	 * @return Der Pfad zur Datei. Z.B. "Music/song.mp3"
	 */
	public abstract String getPath();

	/**
	 * @return Den Namen der Datei (ohne Pfadangabe). Z.B. "song.mp3"
	 */
	public abstract String getName();

	/**
	 * @return True, wenn die Datei ein Verzeichnis ist.
	 */
	public abstract boolean isDir();

	/**
	 * @return Den MIME-Type der Datei oder null, wenn dieser nicht bestimmt werden konnte oder es sich bei der Datei um einen Ordner handelt.
	 */
	public abstract String getContentType();

	/**
	 * @return Die Dateigröße in Bytes.
	 */
	public abstract long getSize();

	/**
	 * @return Den Zeitpunkt der letzten Modifikation in Millisekunden seit dem 01.01.1970.
	 */
	public abstract long getLastModified();

	/**
	 * @return Den Zeitpunkt, zu dem diese FileInfo erstellt wurde in Millisekunden seit dem 01.01.1970.
	 */
	public abstract long getInfoTime();

	/**
	 * Der Boolean-Wert gibt an, ob die Datei vom Anwender modifiziert werden kann. Beispielsweise können die Standard-Ordner (Music, Pictures,
	 * Videos) nicht verändert werden.
	 * 
	 * @return Ob die Datei verändert werden kann.
	 */
	public abstract boolean isModifiable();

	/**
	 * Wichtig fürs Streamen.
	 * 
	 * @return Aktuelle Position in der Mediadatei in Millisekunden.
	 */
	public abstract long getElapsedTime();

	public final boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof FileInfo)
		{
			if (((FileInfo) obj).getPath().equals(this.getPath()))
				return true;
		}
		return false;
	}

	public final int hashCode()
	{
		return getPath().hashCode();
	}

	public final String toString()
	{
		return String.format("FileInfo[" + getPath() + "]");
	}

	/**
	 * @param full
	 *            Ist dieser Wert auf "wahr" gesetzt, werden alle Werte ausgegeben. Ansonsten nur der Pfad.
	 * @return Eine toString-Repräsentation der FileInfo.
	 */
	public final String toString(boolean full)
	{
		if (!full)
			return toString();
		else
			return String.format("FileInfo[path=%s,contentType=%s,dir=%s,size=%d,lastModified=%d,modifiable=%s,elapsedTime=%d,infoTime=%d]",
					getPath(),
					getContentType(),
					isDir(),
					getSize(),
					getLastModified(),
					isModifiable(),
					getElapsedTime(),
					getInfoTime());
	}
}
