/* Copyright (C) 2004 - 2008  db4objects Inc.  http://www.db4o.com

This file is part of the sharpen open source java to c# translator.

sharpen is free software; you can redistribute it and/or modify it under
the terms of version 2 of the GNU General Public License as published
by the Free Software Foundation and as clarified by db4objects' GPL 
interpretation policy, available at
http://www.db4o.com/about/company/legalpolicies/gplinterpretation/
Alternatively you can write to db4objects, Inc., 1900 S Norfolk Street,
Suite 350, San Mateo, CA 94403, USA.

sharpen is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. */

/* Copyright (C) 2004 - 2006 db4objects Inc. http://www.db4o.com */

package sharpen.core;

import sharpen.core.framework.*;
import sharpen.core.io.IO;

class SharpenCommandLineParser extends CommandLineParser {
	
	private final SharpenCommandLine _cmdLine;
	
	public SharpenCommandLineParser(String[] args) {
		this(args, new SharpenCommandLine());
	}
	
	private SharpenCommandLineParser(String[] args, SharpenCommandLine cmdLine) {
		super(args);		
		_cmdLine = cmdLine;
		parse();
	}

	@Override
	protected void processResponseFile(String arg) {
		new SharpenCommandLineParser(
				IO.linesFromFile(arg.substring(1)),
				_cmdLine);
	}

	@Override
	protected void validate() {
		if (_cmdLine.project == null) {
			illegalArgument("unspecified source folder");
		}
	}

	@Override
	protected void processArgument(String arg) {
		if (_cmdLine.project != null) {
			illegalArgument(arg);
		}
		
		if (arg.indexOf('/') > -1) {
			String projectName = arg.split("/")[0];
			String srcFolder = arg.substring(projectName.length() + 1);
			
			_cmdLine.project = projectName;
			_cmdLine.sourceFolders.add(srcFolder);
		} else {
			_cmdLine.project = arg;
		}
	}

	@Override
	protected void processOption(String arg) {
		if (areEqual(arg, "-pascalCase")) {
			_cmdLine.pascalCase = SharpenCommandLine.PascalCaseOptions.Identifiers;
		} else if (areEqual(arg, "-pascalCase+")) {
			_cmdLine.pascalCase = SharpenCommandLine.PascalCaseOptions.NamespaceAndIdentifiers;			 
		} else if (areEqual(arg, "-cp")) {
			_cmdLine.classpath.add(consumeNext());
		} else if (areEqual(arg, "-srcfolder")) {
			_cmdLine.sourceFolders.add(consumeNext());
		} else if (areEqual(arg, "-nativeTypeSystem")) {
			_cmdLine.nativeTypeSystem = true;
		} else if (areEqual(arg, "-nativeInterfaces")) {
			_cmdLine.nativeInterfaces = true;
		} else if (areEqual(arg, "-organizeUsings")) {
			_cmdLine.organizeUsings = true;
		} else if (areEqual(arg, "-fullyQualify")) {
			_cmdLine.fullyQualifiedTypes.add(consumeNext());
		} else if (areEqual(arg, "-namespaceMapping")) {
			String from = consumeNext();
			String to = consumeNext();
			_cmdLine.namespaceMappings.add(new Configuration.NameMapping(from, to));
		} else if (areEqual(arg, "-methodMapping")) {
			String from = consumeNext();
			String to = consumeNext();
			_cmdLine.memberMappings.put(from, new Configuration.MemberMapping(to, MemberKind.Method));
		} else if (areEqual(arg, "-typeMapping")) {
			String from = consumeNext();
			String to = consumeNext();
			_cmdLine.typeMappings.add(new Configuration.NameMapping(from, to));
		} else if (areEqual(arg, "-propertyMapping")) {
			String from = consumeNext();
			String to = consumeNext();
			_cmdLine.memberMappings.put(from, new Configuration.MemberMapping(to, MemberKind.Property));
		} else if (areEqual(arg, "-runtimeTypeName")){
			_cmdLine.runtimeTypeName = consumeNext();
		} else if (areEqual(arg, "-header")){
			_cmdLine.headerFile = consumeNext();
		} else if (areEqual(arg, "-xmldoc")){
			_cmdLine.xmldoc = consumeNext();
		} else {
			illegalArgument(arg);
		}
	}

	public SharpenCommandLine commandLine() {
		return _cmdLine;
	}

}