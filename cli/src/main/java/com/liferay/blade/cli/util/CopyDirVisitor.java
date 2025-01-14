/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.cli.util;

import java.io.IOException;

import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;

/**
 * @author Gregory Amerson
 */
public class CopyDirVisitor extends SimpleFileVisitor<Path> {

	public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption) {
		_fromPath = fromPath;
		_toPath = toPath;
		_copyOption = copyOption;

		_deleteSource = false;
	}

	public CopyDirVisitor(Path fromPath, Path toPath, CopyOption copyOption, boolean deleteSource) {
		_fromPath = fromPath;
		_toPath = toPath;
		_copyOption = copyOption;
		_deleteSource = deleteSource;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path file, IOException ioException) throws IOException {
		if (_deleteSource) {
			FileUtils.forceDelete(file.toFile());
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		Path targetPath = _toPath.resolve(_fromPath.relativize(dir));

		if (!Files.exists(targetPath)) {
			Files.createDirectory(targetPath);
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.copy(file, _toPath.resolve(_fromPath.relativize(file)), _copyOption);

		return FileVisitResult.CONTINUE;
	}

	private final CopyOption _copyOption;
	private boolean _deleteSource;
	private final Path _fromPath;
	private final Path _toPath;

}